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

package xyz.zedler.patrick.doodle.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener;
import java.util.HashMap;
import xyz.zedler.patrick.doodle.Constants;
import xyz.zedler.patrick.doodle.Constants.DEF;
import xyz.zedler.patrick.doodle.Constants.NIGHT_MODE;
import xyz.zedler.patrick.doodle.Constants.PREF;
import xyz.zedler.patrick.doodle.Constants.WALLPAPER;
import xyz.zedler.patrick.doodle.R;
import xyz.zedler.patrick.doodle.activity.MainActivity;
import xyz.zedler.patrick.doodle.behavior.ScrollBehavior;
import xyz.zedler.patrick.doodle.behavior.SystemBarBehavior;
import xyz.zedler.patrick.doodle.databinding.FragmentAppearanceBinding;
import xyz.zedler.patrick.doodle.util.ViewUtil;
import xyz.zedler.patrick.doodle.util.WallpaperUtil;
import xyz.zedler.patrick.doodle.view.SelectionCardView;
import xyz.zedler.patrick.doodle.wallpaper.AnthonyWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.AutumnWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.BaseWallpaper;
import xyz.zedler.patrick.doodle.wallpaper.BaseWallpaper.WallpaperVariant;
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

public class AppearanceFragment extends BaseFragment implements OnButtonCheckedListener {

  private FragmentAppearanceBinding binding;
  private MainActivity activity;
  private BaseWallpaper currentWallpaper;
  private boolean isWallpaperNightMode;
  private final HashMap<String, SelectionCardView> designSelections = new HashMap<>();

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
  ) {
    binding = FragmentAppearanceBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    activity = (MainActivity) requireActivity();

    SystemBarBehavior systemBarBehavior = new SystemBarBehavior(activity);
    systemBarBehavior.setAppBar(binding.appBarAppearance);
    systemBarBehavior.setScroll(binding.scrollAppearance, binding.constraintAppearance);
    systemBarBehavior.setAdditionalBottomInset(activity.getFabTopEdgeDistance());
    systemBarBehavior.setUp();

    new ScrollBehavior().setUpScroll(
        binding.appBarAppearance, binding.scrollAppearance, true
    );

    binding.toolbarAppearance.setNavigationOnClickListener(getNavigationOnClickListener());
    binding.toolbarAppearance.setOnMenuItemClickListener(getOnMenuItemClickListener());

    int idNightMode;
    switch (getSharedPrefs().getInt(PREF.NIGHT_MODE, DEF.NIGHT_MODE)) {
      case NIGHT_MODE.ON:
        idNightMode = R.id.button_appearance_night_mode_on;
        break;
      case NIGHT_MODE.OFF:
        idNightMode = R.id.button_appearance_night_mode_off;
        break;
      default:
        idNightMode = R.id.button_appearance_night_mode_auto;
        break;
    }
    binding.toggleAppearanceNightMode.check(idNightMode);
    binding.toggleAppearanceNightMode.addOnButtonCheckedListener(this);
    isWallpaperNightMode = isWallpaperNightMode();
    binding.imageAppearanceNightMode.setImageResource(
        isWallpaperNightMode
            ? R.drawable.ic_rounded_dark_mode_to_light_mode_anim
            : R.drawable.ic_rounded_light_mode_to_dark_mode_anim
    );

    setUpDesignSelections();
  }

  @Override
  public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
    if (!isChecked) {
      return;
    }
    performHapticClick();
    if (group.getId() == R.id.toggle_appearance_night_mode) {
      int pref;
      if (checkedId == R.id.button_appearance_night_mode_on) {
        pref = NIGHT_MODE.ON;
      } else if (checkedId == R.id.button_appearance_night_mode_off) {
        pref = NIGHT_MODE.OFF;
      } else {
        pref = NIGHT_MODE.AUTO;
      }
      getSharedPrefs().edit().putInt(PREF.NIGHT_MODE, pref).apply();
      boolean isNewWallpaperNightMode = isWallpaperNightMode();
      if (isWallpaperNightMode != isNewWallpaperNightMode) {
        ViewUtil.startIcon(binding.imageAppearanceNightMode);
        new Handler(Looper.getMainLooper()).postDelayed(
            () -> {
              if (binding != null) {
                binding.imageAppearanceNightMode.setImageResource(
                    isNewWallpaperNightMode
                        ? R.drawable.ic_rounded_dark_mode_to_light_mode_anim
                        : R.drawable.ic_rounded_light_mode_to_dark_mode_anim
                );
              }
            },
            300
        );
        isWallpaperNightMode = isNewWallpaperNightMode;
      }
    }
  }

  private void setUpDesignSelections() {
    BaseWallpaper[] baseWallpapers;
    ViewGroup container;

    for (int i = 0; i < 3; i++) {
      if (i == 0) {
        baseWallpapers = new BaseWallpaper[]{
            new PixelWallpaper(),
            new JohannaWallpaper(),
            new ReikoWallpaper(),
            new AnthonyWallpaper()
        };
        container = binding.linearAppearanceWallpaperDoodle;
      } else if (i == 1) {
        baseWallpapers = new BaseWallpaper[]{
            new FloralWallpaper(),
            new AutumnWallpaper(),
            new StoneWallpaper(),
            new WaterWallpaper(),
            new SandWallpaper(),
            new MonetWallpaper(),
            new OrioleWallpaper()
        };
        container = binding.linearAppearanceWallpaperMonet;
      } else {
        baseWallpapers = new BaseWallpaper[]{
            new LeafyWallpaper(),
            new FogWallpaper(),
            new LeavesWallpaper()
        };
        container = binding.linearAppearanceWallpaperAnna;
      }

      for (BaseWallpaper wallpaper : baseWallpapers) {
        SelectionCardView card = new SelectionCardView(activity);
        card.setScrimEnabled(true, false);
        card.setCardImageResource(wallpaper.getThumbnailResId(), false);
        card.setOnClickListener(v -> {
          if (card.isChecked()) {
            return;
          }
          ViewUtil.startIcon(binding.imageAppearanceWallpaper);
          card.startCheckedIcon();
          performHapticClick();
          ViewUtil.uncheckAllChildren(
              binding.linearAppearanceWallpaperDoodle,
              binding.linearAppearanceWallpaperMonet,
              binding.linearAppearanceWallpaperAnna
          );
          card.setChecked(true);
          int oldCount = currentWallpaper != null ? currentWallpaper.getVariants().length : 0;
          currentWallpaper = wallpaper;
          refreshVariantSelection(oldCount, wallpaper, true);
          getSharedPrefs().edit().putString(PREF.WALLPAPER, wallpaper.getName()).apply();
        });

        boolean isSelected = getSharedPrefs().getString(
            PREF.WALLPAPER, WALLPAPER.PIXEL
        ).equals(wallpaper.getName());
        card.setChecked(isSelected);
        if (isSelected) {
          int oldCount = currentWallpaper != null ? currentWallpaper.getVariants().length : 0;
          currentWallpaper = wallpaper;
          refreshVariantSelection(oldCount, wallpaper, false);
        }

        container.addView(card);
        designSelections.put(wallpaper.getName(), card);
      }
    }
  }

  private void refreshVariantSelection(int oldCount, BaseWallpaper wallpaper, boolean animated) {
    if (animated) {
      if (oldCount == wallpaper.getVariants().length) {
        replaceVariantContainer(wallpaper, true);
      } else {
        binding.linearAppearanceVariantContainer.animate().alpha(0).withEndAction(() -> {
          replaceVariantContainer(wallpaper, false);
          binding.linearAppearanceVariantContainer.animate().alpha(1).setDuration(150).start();
        }).setDuration(150).start();
      }
    } else {
      binding.linearAppearanceVariantContainer.setAlpha(1);
      replaceVariantContainer(wallpaper, oldCount == wallpaper.getVariants().length);
    }
  }

  private void replaceVariantContainer(BaseWallpaper wallpaper, boolean sameCount) {
    if (binding == null) {
      return;
    }
    if (!sameCount) {
      binding.linearAppearanceVariantContainer.removeAllViews();
    }
    for (int i = 0; i < wallpaper.getVariants().length; i++) {
      final int iFinal = i;
      WallpaperVariant variantLight = wallpaper.getVariants()[iFinal];

      SelectionCardView card;
      if (sameCount) {
        SelectionCardView child
            = (SelectionCardView) binding.linearAppearanceVariantContainer.getChildAt(i);
        card = child != null ? child : new SelectionCardView(activity);
      } else {
        card = new SelectionCardView(activity);
      }

      if (sameCount) {
        ValueAnimator animator = ValueAnimator.ofArgb(
            card.getCardBackgroundColor().getDefaultColor(), variantLight.getPrimaryColor()
        );
        animator.addUpdateListener(animation -> {
          card.setCardBackgroundColor((int) animation.getAnimatedValue());
          card.setScrimEnabled(false, true);
        });
        animator.setDuration(300);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
      } else {
        card.setCardBackgroundColor(variantLight.getPrimaryColor());
        card.setScrimEnabled(false, true);
      }
      card.setOnClickListener(v -> {
        if (!card.isChecked()) {
          ViewUtil.startIcon(binding.imageAppearanceVariant);
          card.startCheckedIcon();
          performHapticClick();
          ViewUtil.uncheckAllChildren(binding.linearAppearanceVariantContainer);
          card.setChecked(true);
          getSharedPrefs().edit()
              .putInt(Constants.VARIANT_PREFIX + wallpaper.getName(), iFinal)
              .apply();
        }
      });
      boolean isSelected = getSharedPrefs().getInt(
          Constants.VARIANT_PREFIX + wallpaper.getName(), 0
      ) == iFinal;
      card.setChecked(isSelected);
      if (!sameCount) {
        binding.linearAppearanceVariantContainer.addView(card);
      }
    }
  }

  private boolean isWallpaperNightMode() {
    return WallpaperUtil.isWallpaperNightMode(activity, getSharedPrefs());
  }
}
