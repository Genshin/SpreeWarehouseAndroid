package org.genshin.warehouse.products;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.genshin.spree.ScanSystem;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.products.ProductDetailsActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsMenuActivity extends Activity {
	private static Products products;
	private static Product selectedProduct;
	private SpreeConnector spree;
	
	private ProductListAdapter productsAdapter;
	
	private ListView productList;
	private TextView statusText;
	private MultiAutoCompleteTextView searchBar;
	private Button searchButton;
	
	private Button clearButton;
	
	private ImageButton scanButton;
	private Spinner orderSpinner;
	private ArrayAdapter<String> sadapter;
	
	private ImageButton backwardButton;
	private boolean updown = false;		// falseの時は▽、trueの時は△表示
	
	private void hookupInterface() {
		productList = (ListView) findViewById(R.id.product_menu_list);
        statusText = (TextView) findViewById(R.id.status_text);
        searchBar = (MultiAutoCompleteTextView) findViewById(R.id.product_menu_searchbox);
		
        //Visual Code Scan hookup
		scanButton = (ImageButton) findViewById(R.id.products_menu_scan_button);
		scanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		clearImage();
        		orderSpinner.setSelection(0);
        		Toast.makeText(v.getContext(), getString(R.string.scan), Toast.LENGTH_LONG).show();
                ScanSystem.initiateScan(v.getContext());
            }
		});
		
		//Standard text search hookup
		searchButton = (Button) findViewById(R.id.products_menu_search_button);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				products.textSearch(searchBar.getText().toString());
				refreshProductMenu();
				clearImage();
				orderSpinner.setSelection(0);
			}
		});
		
		//Clear button
		clearButton = (Button) findViewById(R.id.products_menu_clear_button);
		clearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				searchBar.setText("");
				products.clear();
				refreshProductMenu();
				clearImage();
				orderSpinner.setSelection(0);
			}
		});
		
		//Order spinner
		orderSpinner = (Spinner) findViewById(R.id.order_spinner);
		sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	    sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // アイテムを追加します
	    sadapter.add("未選択");
	    sadapter.add("初期値に戻す");
	    sadapter.add("名前順");
	    sadapter.add("値段順");
	    sadapter.add("在庫数順");
	    orderSpinner.setPrompt("ソート");
	    orderSpinner.setAdapter(sadapter);
	    
	    orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner) parent;
                //String item = (String) spinner.getSelectedItem();
                switch(position) {
                	case 0:		// 未選択
                		break;
                	case 1:		// 初期値に戻す
                		products.textSearch(searchBar.getText().toString());
        				refreshProductMenu();
                		clearImage();
                		break;
                	case 2:		// 名前順
						sortName();
						clearImage();
                		break;
	                case 3:		// 値段順
	                	sortPrice();
	                	clearImage();
	                	break;
	                case 4:		// 在庫数順
	                	sortCountOnHand();
	                	clearImage();
	                	break;
	                default :
	                	break;             
                }
            }

			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
	    
	    // backwardButton 並びを逆に
	    backwardButton = (ImageButton)findViewById(R.id.product_menu_backward_button);
	    backwardButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				if (!updown) {
					backwardButton.setImageResource(android.R.drawable.arrow_up_float);
					updown = true;
				} else {
					backwardButton.setImageResource(android.R.drawable.arrow_down_float);
					updown = false;
				}
				switchOrder();
			}
		});
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        Warehouse.ChangeActivityContext(this);
        
        hookupInterface();
        
        spree = new SpreeConnector(this);
        if (products == null) {
        	products = new Products(this, spree);
        	products.getNewestProducts(10);
        }
        
        refreshProductMenu();
        
	}

	public static enum menuCodes { registerProduct };

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Resources res = getResources();
        // メニューアイテムを追加する
        menu.add(Menu.NONE, menuCodes.registerProduct.ordinal(), Menu.NONE, res.getString(R.string.register_product));
        return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == menuCodes.registerProduct.ordinal()) {
			Intent intent = new Intent(this, ProductEditActivity.class);
			intent.putExtra("IS_NEW", true);
            startActivity(intent);
        	
			return true;
		}
        
        return false;
    }
	
	private void refreshProductMenu() {		
		ProductListItem[] productListItems = new ProductListItem[products.list.size()];
		
		for (int i = 0; i < products.list.size(); i++) {
			Product p = products.list.get(i);
			Drawable thumb = null;
			if (p.thumbnail != null)
				thumb = p.thumbnail.data;
			
			productListItems[i] = new ProductListItem(thumb, p.name, p.sku, p.countOnHand, p.permalink, p.price, p.id);
		}
		
		statusText.setText(products.count + this.getString(R.string.products_counter) );
		
		productsAdapter = new ProductListAdapter(this, productListItems);
		productList.setAdapter(productsAdapter);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	productListClickHandler(parent, view, position);
            }
        });
        
	}
	
	public static void showProductDetails(Context ctx, Product product) {
		ProductsMenuActivity.setSelectedProduct(product);
		Intent productDetailsIntent = new Intent(ctx, ProductDetailsActivity.class);
    	ctx.startActivity(productDetailsIntent);
	}
	
	private void productListClickHandler(AdapterView<?> parent, View view, int position) {
		ProductsMenuActivity.showProductDetails(this, products.list.get(position));				
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == ResultCodes.SCAN.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //TODO limit this to bar code types?
                if (format != "QR_CODE") {
                	//Assume barcode, and barcodes correlate to products
                	//Toast.makeText(this, "[" + format + "]: " + contents + "\nSearching!", Toast.LENGTH_LONG).show();
                	products.findByBarcode(contents);
                	//if we have one hit that's the product we want, so go to it
                	refreshProductMenu();
                	if (products.list.size() == 1)
                		showProductDetails(this, products.list.get(0));
                    
                	//Toast.makeText(this, "Results:" + products.count, Toast.LENGTH_LONG).show();
                }
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

	public static Product getSelectedProduct() {
		return selectedProduct;
	}

	public static void setSelectedProduct(Product selectedProduct) {
		if (selectedProduct == null)
			selectedProduct = new Product(0, "", "", 0, 0, "", "");
		
		ProductsMenuActivity.selectedProduct = selectedProduct;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	//
	// 各種ソート
	//
	/////////////////////////////////////////////////////////////////////////////////////

	// ▽ボタンを初期に
	
	public void clearImage() {
		if (updown) {
			backwardButton.setImageResource(android.R.drawable.arrow_down_float);
			updown = false;
		}
	}
	
	// 並びを逆にする
	public void switchOrder() {
		ArrayList<Product> sortedList = new ArrayList<Product>();
		
		for (int i = products.list.size() - 1; i >= 0; i--) {
			sortedList.add(products.list.get(i));
		}
		
		products.list = sortedList;
		refreshProductMenu();
	}
	
	// 値段順
	public void sortPrice() {
		Product temp;

		for (int i = 0; i < products.list.size() - 1; i++) {
			for (int j = i + 1; j < products.list.size(); j++) {
				if (products.list.get(i).price < products.list.get(j).price) {
					temp = products.list.get(i);
					products.list.set(i, products.list.get(j));
					products.list.set(j, temp);
				} else if (products.list.get(i).price == products.list.get(j).price) {
					if (products.list.get(i).id > products.list.get(j).id) {
						temp = products.list.get(i);
						products.list.set(i, products.list.get(j));
						products.list.set(j, temp);
					}
				}
			}
		}

		refreshProductMenu();
	}
	
	// 在庫数順
	public void sortCountOnHand() {
		Product temp;
		
		for (int i = 0; i < products.list.size() - 1; i++) {
			for (int j = i + 1; j < products.list.size(); j++) {
				if (products.list.get(i).countOnHand < products.list.get(j).countOnHand) {
					temp = products.list.get(i);
					products.list.set(i, products.list.get(j));
					products.list.set(j, temp);
				} else if (products.list.get(i).countOnHand == products.list.get(j).countOnHand) {
					if (products.list.get(i).id > products.list.get(j).id) {
						temp = products.list.get(i);
						products.list.set(i, products.list.get(j));
						products.list.set(j, temp);
					}
				}
			}
		}

		refreshProductMenu();
	}
	
	// 名前順
	public void sortName(){
		Product temp;
		
		for (int i = 0; i < products.list.size() - 1; i++) {
			for (int j = i + 1; j < products.list.size(); j++) {
				if (products.list.get(i).name.compareTo(products.list.get(j).name) > 0) {
					temp = products.list.get(i);
					products.list.set(i, products.list.get(j));
					products.list.set(j, temp);
				} else if (products.list.get(i).name.compareTo(products.list.get(j).name) == 0) {
					if (products.list.get(i).id > products.list.get(j).id) {
						temp = products.list.get(i);
						products.list.set(i, products.list.get(j));
						products.list.set(j, temp);
					}
				}
			}
		}

		refreshProductMenu();
	}
	
	
	/*
	// 名前順
	public void sortName(){
		ArrayList<Product> sortedList = new ArrayList<Product>();
		String[][] sort = new String[products.list.size()][2];
		
		for (int i = 0; i < products.list.size(); i++) {
				sort[i][0] = products.list.get(i).name;
				sort[i][1] = String.valueOf(products.list.get(i).id);
		}
		
		Arrays.sort(sort, new nameComparator());
		
		for (int i = 0; i <products.list.size(); i++) {
			for (int j = 0; j < products.list.size(); j++)
				if (sort[i][0].equals(products.list.get(j).name) &&
					sort[i][1] == String.valueOf(products.list.get(j).id)) {
					sortedList.add(products.list.get(j));
				}
		}
		
		products.list = sortedList;
		refreshProductMenu();
	}		
	// 比較用
	public class nameComparator implements Comparator<Object> {
		private int index;

		public int compare(Object Obj1, Object Obj2) {
			index = 0;

			String[] str1 = (String[])Obj1;
			String[] str2 = (String[])Obj2;
			
			if ((str1[index].compareTo(str2[index])) == 0) {
				index = 1;
			}
			
			return (str1[index].compareTo(str2[index]));
		}
		
	}
	*/

}
