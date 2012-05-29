package org.genshin.warehouse.products;

public class Variant {
	public int		id;
	public String	name;
	public int		countOnHand;
	public String	visualCode;
	public String	sku;
	public double	price;
	public double	weight;
	public double	height;
	public double	width;
	public double	depth;
	public Boolean	isMaster;
	public double	costPrice;
	public String	permalink;
	//TODO options
	
	//dummy init
	public Variant () {
		this.id = -1;
		this.name = "";
		this.countOnHand = 0;
		this.visualCode = "";
		this.sku = "";
		this.price = 0.0;
		this.weight = 0.0;
		this.height = 0.0;
		this.width = 0.0;
		this.depth = 0.0;
		this.isMaster = false;
		this.costPrice = 0.0;
		this.permalink = "";
	}


	public Variant(int id, String name, int countOnHand, String visualCode,
			String sku, double price, double weight, double height, double width,
			double depth, Boolean isMaster, double costPrice, String permalink) {

			this.id = id;
			this.name = name;
			this.countOnHand = countOnHand;
			this.visualCode = visualCode;
			this.sku = sku;
			this.price = price;
			this.weight = weight;
			this.height = height;
			this.width = width;
			this.depth = depth;
			this.isMaster = isMaster;
			this.costPrice = costPrice;
			this.permalink = permalink;
	}
	
	
}
