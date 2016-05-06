package com.qws.nypp.view.pullview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recyclerView的间距
 * 
 * @Description
 * @author Troy
 * @date 2016-5-5 下午4:37:38
 * @Copyright:
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    	if(parent.getChildPosition(view)!=0){
    		outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            outRect.top=space;
            if(parent.getChildPosition(view)==2){
    			outRect.top=space * 2;
    		}
    	}
        
    }
}