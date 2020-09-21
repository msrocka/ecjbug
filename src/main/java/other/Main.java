package other;

import model.Container;
import model.PublicItem;

public class Main {

	public static void main(String[] args) {
		var container = new Container();
		container.items.add(new PublicItem());
		for (var item : container.items) {
			if (item instanceof PublicItem) {
				var publicItem = (PublicItem) item;
				System.out.println(publicItem);
			}
		}
	}
}
