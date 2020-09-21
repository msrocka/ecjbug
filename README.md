See [Eclipse Bug 567183](https://bugs.eclipse.org/bugs/show_bug.cgi?id=567183)

The Eclipse Compiler for Java (ECJ) should throw a compiler error
when a `var` declaration refers to a package private type from another package.
But there is at least one case where this is not correctly done and an
`IllegalAccessError` is thrown at runtime instead. Here is a simple example that
illustrates that issue: There is a package `model` with the following classes:

```java
// Item.java
class Item {
}

// Container.java
import java.util.ArrayList;

public class Container {
  public final ArrayList<Item> items = new ArrayList<Item>();
}

// PublicItem.java
public class PublicItem extends Item {
}
```

So `Container` uses the package private type `Item` as type parameter for its
public list field `items`. And there is a class `PublicItem` that extends `Item`.
Then, this is used from another package in the following way:

```java
var container = new Container();
container.items.add(new PublicItem());
for (var item : container.items) {
  if (item instanceof PublicItem) {
    var publicItem = (PublicItem) item;
    System.out.println(publicItem);
  }
}
```

Compiling this with OpenJDK 14.0.1 gives the following compiler error:

```
Compilation failure
[ERROR] ... model.Item is not public in model; cannot be accessed from outside package
```

But ECJ compiles this without any error. Running the compiled code gives an
`IllegalAccessError` at runtime instead:

```
Exception in thread "main" java.lang.IllegalAccessError:
failed to access class model.Item from class other.Main
(model.Item and other.Main are in unnamed module of loader 'app')
	at other.Main.main(Main.java:11)
```


