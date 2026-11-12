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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider.OnChangeListener;
import xyz.zedler.patrick.doodle.Constants.PREF;
import xyz.zedler.patrick.doodle.R;
import xyz.zedler.patrick.doodle.activity.MainActivity;
import xyz.zedler.patrick.doodle.behavior.ScrollBehavior;
import xyz.zedler.patrick.doodle.behavior.SystemBarBehavior;
import xyz.zedler.patrick.doodle.databinding.FragmentSizeBinding;
import xyz.zedler.patrick.doodle.drawable.SvgDrawable;
import xyz.zedler.patrick.doodle.util.ViewUtil;

public class SizeFragment extends BaseFragment implements OnClickListener, OnChangeListener {

  private FragmentSizeBinding binding;
  private MainActivity activity;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
  ) {
    binding = FragmentSizeBinding.inflate(inflater, container, false);
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
    systemBarBehavior.setAppBar(binding.appBarSize);
    systemBarBehavior.setScroll(binding.scrollSize, binding.constraintSize);
    systemBarBehavior.setAdditionalBottomInset(activity.getFabTopEdgeDistance());
    systemBarBehavior.setUp();

    new ScrollBehavior().setUpScroll(binding.appBarSize, binding.scrollSize, true);

    binding.toolbarSize.setNavigationOnClickListener(getNavigationOnClickListener());
    binding.toolbarSize.setOnMenuItemClickListener(getOnMenuItemClickListener());

    binding.sliderSizeScale.setValue(
        getSharedPrefs().getFloat(PREF.SCALE, SvgDrawable.getDefaultScale(activity)) * 10
    );
    binding.sliderSizeScale.addOnChangeListener(this);
    binding.sliderSizeScale.setLabelFormatter(value -> {
      float scale = value / 10f;
      return String.format(
          activity.getLocale(), scale == 1 || scale == 2 ? "×%.0f" : "×%.1f", scale
      );
    });

    ViewUtil.setOnClickListeners(
        this,
        binding.buttonSizeScaleReset
    );
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.button_size_scale_reset) {
      float def = SvgDrawable.getDefaultScale(activity);
      float scaleNew = def * 10;
      binding.sliderSizeScale.setValue(scaleNew);
      getSharedPrefs().edit().putFloat(PREF.SCALE, def).apply();
      performHapticClick();
      ViewUtil.startIcon(binding.buttonSizeScaleReset.getIcon());
    }
  }

  @Override
  public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
    if (!fromUser) {
      return;
    }
    int id = slider.getId();
    if (id == R.id.slider_size_scale) {
      getSharedPrefs().edit().putFloat(PREF.SCALE, value / 10).apply();
      performHapticClick();
    }
  }
}
