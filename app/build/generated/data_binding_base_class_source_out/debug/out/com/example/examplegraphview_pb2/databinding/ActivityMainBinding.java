// Generated by view binder compiler. Do not edit!
package com.example.examplegraphview_pb2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.examplegraphview_pb2.CircularProgress;
import com.example.examplegraphview_pb2.GraphView;
import com.example.examplegraphview_pb2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button changeProgressBtn;

  @NonNull
  public final CircularProgress circularProgress;

  @NonNull
  public final GraphView graphView;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button changeProgressBtn,
      @NonNull CircularProgress circularProgress, @NonNull GraphView graphView) {
    this.rootView = rootView;
    this.changeProgressBtn = changeProgressBtn;
    this.circularProgress = circularProgress;
    this.graphView = graphView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.changeProgressBtn;
      Button changeProgressBtn = ViewBindings.findChildViewById(rootView, id);
      if (changeProgressBtn == null) {
        break missingId;
      }

      id = R.id.circularProgress;
      CircularProgress circularProgress = ViewBindings.findChildViewById(rootView, id);
      if (circularProgress == null) {
        break missingId;
      }

      id = R.id.graphView;
      GraphView graphView = ViewBindings.findChildViewById(rootView, id);
      if (graphView == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, changeProgressBtn,
          circularProgress, graphView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}