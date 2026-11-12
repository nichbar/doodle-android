/*
 * This file is part of Doodle Android.
 *
 * Doodle Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Doodle Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Doodle Android. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2019-2025 by Patrick Zedler
 */

package xyz.zedler.patrick.doodle.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import xyz.zedler.patrick.doodle.Constants;
import xyz.zedler.patrick.doodle.Constants.DEF;
import xyz.zedler.patrick.doodle.Constants.NIGHT_MODE;
import xyz.zedler.patrick.doodle.Constants.PREF;
import xyz.zedler.patrick.doodle.R;
import xyz.zedler.patrick.doodle.drawable.SvgDrawable;
import xyz.zedler.patrick.doodle.wallpaper.BaseWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.BaseWallpaper.WallpaperVariant;

public class WallpaperUtil {

  private static final String TAG = WallpaperUtil.class.getSimpleName();
  private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

  public interface WallpaperCallback {
    void onResult(boolean success);
  }

  public static void applyStaticWallpaper(
      @NonNull Context context,
      @Nullable WallpaperCallback callback
  ) {
    Context appContext = context.getApplicationContext();
    EXECUTOR.execute(() -> {
      boolean success = false;
      Bitmap bitmap = null;
      try {
        SharedPreferences sharedPrefs = new PrefsUtil(appContext).getSharedPrefs();
        BaseWallpaper wallpaper = Constants.getWallpaper(
            sharedPrefs.getString(PREF.WALLPAPER, DEF.WALLPAPER)
        );
        int variantIndex = sharedPrefs.getInt(
            Constants.VARIANT_PREFIX + wallpaper.getName(), 0
        );
        if (variantIndex >= wallpaper.getVariants().length
            || variantIndex >= wallpaper.getDarkVariants().length) {
          variantIndex = 0;
        }

        boolean isNightMode = isWallpaperNightMode(appContext, sharedPrefs);
        WallpaperVariant variant;
        SvgDrawable svgDrawable;
        if (isNightMode) {
          variant = wallpaper.getDarkVariants()[variantIndex];
          svgDrawable = wallpaper.getPreparedSvg(
              new SvgDrawable(appContext, variant.getSvgResId()), variantIndex, true
          );
        } else {
          variant = wallpaper.getVariants()[variantIndex];
          svgDrawable = wallpaper.getPreparedSvg(
              new SvgDrawable(appContext, variant.getSvgResId()), variantIndex, false
          );
        }
        if (svgDrawable == null) {
          svgDrawable = wallpaper.getPreparedSvg(
              new SvgDrawable(appContext, R.raw.wallpaper_pixel1), 1, false
          );
        }

        float scale = sharedPrefs.getFloat(
            PREF.SCALE, SvgDrawable.getDefaultScale(appContext)
        );
        svgDrawable.setScale(scale);

        int width = UiUtil.getDisplayWidth(appContext);
        int height = UiUtil.getDisplayHeight(appContext);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        svgDrawable.draw(canvas);

        WallpaperManager manager = WallpaperManager.getInstance(appContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          manager.setBitmap(
              bitmap,
              null,
              true,
              WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK
          );
        } else {
          manager.setBitmap(bitmap);
        }
        success = true;
      } catch (Throwable e) {
        Log.e(TAG, "applyStaticWallpaper: ", e);
      } finally {
        if (bitmap != null && !bitmap.isRecycled()) {
          bitmap.recycle();
        }
      }

      final boolean finalSuccess = success;
      if (callback != null) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onResult(finalSuccess));
      }
    });
  }

  public static boolean isWallpaperNightMode(
      @NonNull Context context,
      @NonNull SharedPreferences sharedPrefs
  ) {
    int nightMode = sharedPrefs.getInt(PREF.NIGHT_MODE, DEF.NIGHT_MODE);
    if (nightMode == NIGHT_MODE.ON) {
      return true;
    }
    int flags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    return nightMode == NIGHT_MODE.AUTO && flags == Configuration.UI_MODE_NIGHT_YES;
  }
}
