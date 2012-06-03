package org.genshin.gsa;

import org.genshin.warehouse.R;

import android.app.ProgressDialog;
import android.content.Context;

public class Dialogs {

	private static ProgressDialog progress = null;

	private static void showProgress(Context ctx, CharSequence msg) {
		if (progress != null) //dialog already being used!
			dismiss();

		progress = new ProgressDialog(ctx);
		progress.setCancelable(true);
		progress.setMessage(msg);
		progress.show();
	}

	public static void showSearching(Context ctx) {
		showProgress(ctx, ctx.getText(R.string.searching));
	}
	
	public static void showLoading(Context ctx) {
		showProgress(ctx, ctx.getText(R.string.loading));
	}
	
	public static void showConnecting(Context ctx) {
		showProgress(ctx, ctx.getText(R.string.connecting));
	}
	
	public static void dismiss() {
		if (progress != null) {
			progress.dismiss();
			progress = null;
		}
	}
	
}
