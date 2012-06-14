package org.genshin.spree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.genshin.warehouse.Warehouse.ResultCodes;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;

public class ScanSystem{
	
	// supported barcode formats
	public static final Collection<String> PRODUCT_CODE_TYPES = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14");
	public static final Collection<String> ONE_D_CODE_TYPES =
			list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128",
					"ITF", "RSS_14", "RSS_EXPANDED");
	public static final Collection<String> QR_CODE_TYPES = Collections.singleton("QR_CODE");
	public static final Collection<String> DATA_MATRIX_TYPES = Collections.singleton("DATA_MATRIX");

  
	private static Collection<String> list(String... values) {
		return Collections.unmodifiableCollection(Arrays.asList(values));
	}
}
