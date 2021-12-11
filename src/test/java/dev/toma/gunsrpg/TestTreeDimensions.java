package dev.toma.gunsrpg;

import dev.toma.gunsrpg.util.math.TreeDimensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class TestTreeDimensions {

    private static TreeDimensions<TreeObj> treeDimensions;

    @BeforeAll
    public static void initTree() {
        /*
          1.    A
          2.   B  C
          3.   D E F
          4.   G  H I J
          5.        K
         */
        TreeObj k = new TreeObj();
        TreeObj j = new TreeObj();
        TreeObj i = new TreeObj(k);
        TreeObj h = new TreeObj();
        TreeObj g = new TreeObj();
        TreeObj f = new TreeObj(h, i, j);
        TreeObj e = new TreeObj();
        TreeObj d = new TreeObj(g);
        TreeObj c = new TreeObj(e, f);
        TreeObj b = new TreeObj(d);
        TreeObj a = new TreeObj(b, c);
        treeDimensions = new TreeDimensions<>(a, TreeObj::getChildren);
    }

    @Test
    public void testHeight() {
        Assertions.assertEquals(5, treeDimensions.getHeight());
    }

    @Test
    public void testWidth() {
        Assertions.assertEquals(4, treeDimensions.getWidth());
    }

    @Test
    public void testSimpleTree() {
        TreeObj c = new TreeObj();
        TreeObj b = new TreeObj();
        TreeObj a = new TreeObj(b, c);
        TreeDimensions<TreeObj> dimensions = new TreeDimensions<>(a, TreeObj::getChildren);
        Assertions.assertEquals(2, dimensions.getHeight());
        Assertions.assertEquals(2, dimensions.getWidth());
    }

    @Test
    public void testSingleNodeTree() {
        TreeDimensions<TreeObj> dimensions = new TreeDimensions<>(new TreeObj(), TreeObj::getChildren);
        Assertions.assertEquals(1, dimensions.getHeight());
        Assertions.assertEquals(1, dimensions.getWidth());
    }

    private static class TreeObj {
        private final TreeObj[] children;

        public TreeObj(TreeObj... children) {
            this.children = children;
        }

        public TreeObj[] getChildren() {
            return children;
        }
    }
}
