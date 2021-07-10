package ir.hadinemati.steelmarketing.Models.Entity;

public class PictureDO {

    String category;
    String ImagePath;

    public PictureDO(String category, String imagePath) {
        this.category = category;
        ImagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
