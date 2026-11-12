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

package xyz.zedler.patrick.doodle;

import androidx.appcompat.app.AppCompatDelegate;
import xyz.zedler.patrick.doodle.wallpaper.AnthonyWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.AutumnWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.BaseWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.FloralWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.FogWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.JohannaWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.LeafyWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.LeavesWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.MonetWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.OrioleWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.PixelWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.ReikoWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.SandWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.StoneWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.WaterWallpaper;

public final class Constants {

  public static final String VARIANT_PREFIX = "variant_";

  public static final class PREF {

    // Appearance

    public static final String WALLPAPER = "wallpaper";
    public static final String VARIANT_PIXEL = "variant_pixel";
    public static final String VARIANT_JOHANNA = "variant_johanna";
    public static final String VARIANT_REIKO = "variant_reiko";
    public static final String VARIANT_ANTHONY = "variant_anthony";

    public static final String NIGHT_MODE = "night_mode";
    public static final String USE_DARK_TEXT = "dark_text";
    public static final String FORCE_LIGHT_TEXT = "light_text";
    public static final String RANDOM = "random";
    public static final String RANDOM_LIST = "random_list";
    public static final String RANDOM_CURRENT = "random_current_wallpaper";
    public static final String DAILY_TIME = "daily_time";
    public static final String RANDOM_INTERVAL = "random_interval";

    // Parallax

    public static final String SWIPE = "swipe";
    public static final String SWIPE_INTENSITY = "swipe_intensity";
    public static final String POWER_SAVE_SWIPE = "power_save_swipe";
    public static final String TILT = "tilt";
    public static final String TILT_INTENSITY = "tilt_intensity";
    public static final String TILT_REFRESH_RATE = "refresh_rate";
    public static final String DAMPING_TILT = "damping_tilt";
    public static final String THRESHOLD = "threshold";
    public static final String POWER_SAVE_TILT = "power_save_tilt";

    // Size

    public static final String SCALE = "size";
    public static final String ZOOM = "zoom";
    public static final String ZOOM_ROTATION = "zoom_rotation";
    public static final String POWER_SAVE_ZOOM = "power_save_zoom";
    public static final String ZOOM_LAUNCHER = "zoom_launcher";
    public static final String USE_ZOOM_DAMPING = "use_zoom_damping";
    public static final String DAMPING_ZOOM = "damping_zoom";
    public static final String ZOOM_UNLOCK = "zoom_unlock";
    public static final String ZOOM_UNLOCK_IN = "zoom_unlock_mode_in";
    public static final String ZOOM_DURATION = "zoom_duration";

    // Other

    public static final String LANGUAGE = "language";
    public static final String GPU = "hardware_acceleration";
    public static final String THEME = "app_theme";
    public static final String UI_MODE = "mode";
    public static final String UI_CONTRAST = "ui_contrast";
    public static final String SCREEN_OFF_DELAY = "screen_off_delay";

    public static final String LAST_VERSION = "last_version";
    public static final String FEEDBACK_POP_UP_COUNT = "feedback_pop_up_count";
    public static final String STATIC_WALLPAPER_APPLIED = "static_wallpaper_applied";
  }

  public static final class DEF {

    public static final String WALLPAPER = Constants.WALLPAPER.PIXEL;
    public static final int NIGHT_MODE = Constants.NIGHT_MODE.AUTO;
    public static final String LANGUAGE = null;
    public static final String THEME = "";
    public static final int UI_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    public static final String UI_CONTRAST = CONTRAST.STANDARD;
  }

  public static BaseWallpaper getWallpaper(String wallpaper) {
    switch (wallpaper) {
      // DOODLE
      case WALLPAPER.JOHANNA:
        return new JohannaWallpaper();
      case WALLPAPER.REIKO:
        return new ReikoWallpaper();
      case WALLPAPER.ANTHONY:
        return new AnthonyWallpaper();
      // MATERIAL YOU
      case WALLPAPER.FLORAL:
        return new FloralWallpaper();
      case WALLPAPER.AUTUMN:
        return new AutumnWallpaper();
      case WALLPAPER.STONE:
        return new StoneWallpaper();
      case WALLPAPER.WATER:
        return new WaterWallpaper();
      case WALLPAPER.SAND:
        return new SandWallpaper();
      case WALLPAPER.MONET:
        return new MonetWallpaper();
      case WALLPAPER.ORIOLE:
        return new OrioleWallpaper();
      // KÖVECSES
      case WALLPAPER.LEAFY:
        return new LeafyWallpaper();
      case WALLPAPER.FOG:
        return new FogWallpaper();
      case WALLPAPER.LEAVES:
        return new LeavesWallpaper();
      default:
        return new PixelWallpaper();
    }
  }

  public static String[] getAllWallpapers() {
    return new String[]{
        WALLPAPER.PIXEL,
        WALLPAPER.JOHANNA,
        WALLPAPER.REIKO,
        WALLPAPER.ANTHONY,

        WALLPAPER.FLORAL,
        WALLPAPER.AUTUMN,
        WALLPAPER.STONE,
        WALLPAPER.WATER,
        WALLPAPER.SAND,
        WALLPAPER.MONET,
        WALLPAPER.ORIOLE,

        WALLPAPER.LEAFY,
        WALLPAPER.FOG,
        WALLPAPER.LEAVES
    };
  }

  public static final class WALLPAPER {

    public static final String PIXEL = "pixel";
    public static final String JOHANNA = "johanna";
    public static final String REIKO = "reiko";
    public static final String ANTHONY = "anthony";

    public static final String STONE = "stone";
    public static final String FLORAL = "floral";
    public static final String AUTUMN = "autumn";
    public static final String WATER = "water";
    public static final String SAND = "sand";
    public static final String MONET = "monet";
    public static final String ORIOLE = "oriole";

    public static final String LEAFY = "leafy";
    public static final String FOG = "fog";
    public static final String LEAVES = "leaves";
  }

  public static final class NIGHT_MODE {

    public static final int AUTO = -1;
    public static final int ON = 1;
    public static final int OFF = 0;
  }

  public static final class EXTRA {

    public static final String RUN_AS_SUPER_CLASS = "run_as_super_class";
    public static final String INSTANCE_STATE = "instance_state";
    public static final String SCROLL_POSITION = "scroll_position";
  }

  public static final class THEME {

    public static final String DYNAMIC = "dynamic";
    public static final String RED = "red";
    public static final String YELLOW = "yellow";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
  }

  public static final class CONTRAST {

    public static final String STANDARD = "standard";
    public static final String MEDIUM = "medium";
    public static final String HIGH = "high";
  }
}
