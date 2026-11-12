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

package xyz.zedler.patrick.doodle.wallpaper;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import xyz.zedler.patrick.doodle.drawable.SvgDrawable;

public abstract class BaseWallpaper {

  public static class WallpaperVariant {

    private final int svgResId;
    private final String primaryColor;

    public WallpaperVariant(@RawRes int resId, @NonNull String primary) {
      svgResId = resId;
      primaryColor = primary;
    }

    public int getSvgResId() {
      return svgResId;
    }

    public int getPrimaryColor() {
      return Color.parseColor(primaryColor);
    }
  }

  @NonNull
  public abstract String getName();

  public abstract int getThumbnailResId();

  @NonNull
  public abstract WallpaperVariant[] getVariants();

  @NonNull
  public abstract WallpaperVariant[] getDarkVariants();

  public SvgDrawable getPreparedSvg(SvgDrawable svgDrawable, int variant, boolean isNightMode) {
    return svgDrawable;
  }
}
