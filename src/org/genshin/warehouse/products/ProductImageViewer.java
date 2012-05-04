package org.genshin.warehouse.products;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class ProductImageViewer implements ViewFactory {
	private Context ctx;
	
	public ProductImageViewer(Context ctx) {
		this.ctx = ctx;
	}

	public View makeView() {
		ImageView imageView = new ImageView(ctx);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new 
                ImageSwitcher.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
        return imageView;
	}

}
