package su.mboh.asu.entity;

/**
 * Created by smktiufa on 17/12/16.
 */

public class Category {

    private int id;
    private String name;
    private String subCategory;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return name;
    }
}
