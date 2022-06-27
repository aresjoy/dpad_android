 package com.genius.baselib.frame.view.widget;

 import android.content.Context;
 import android.util.AttributeSet;
 import android.view.MotionEvent;

 import androidx.viewpager.widget.ViewPager;

 /**
  * Created by chief on 2016-01-05.
  */
 public class CustomViewPager extends ViewPager {

     private boolean scrollable=true;
     private ViewPager viewpager=null;

     public CustomViewPager(Context context) {
         super(context);
     }

     public CustomViewPager(Context context, AttributeSet attrs) {
         super(context, attrs);
     }


     @Override
     public boolean onTouchEvent(MotionEvent ev) {

         if(scrollable){
 //             if(viewpager==null){
 //             }
             return super.onTouchEvent(ev);
         }else{

             return false;
         }

     }


     @Override
     public boolean onInterceptTouchEvent(MotionEvent ev) {


         if(scrollable){
 //            if(viewpager==null){
 //            }
             return super.onInterceptTouchEvent(ev);
         }else{

             return false;
         }

     }


     /**
      * if view is not null,then give view scroll actions
      * @param parentview
      * @param flag
      */
     public void setScrollable(ViewPager parentview, boolean flag){
         scrollable=flag;
         viewpager=parentview;
     }
     public void setScrollable(boolean flag){
         scrollable=flag;
     }
 }
