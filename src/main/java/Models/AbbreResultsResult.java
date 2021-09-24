package Models;

public class AbbreResultsResult
{
    private Double score;
    private String parentcategoryname;
    private String term;
    private String definition;
    private String categoryname;
    private Integer id;
    private String category;
    private String parentcategory;

    public AbbreResultsResult()
    {

    }

    public AbbreResultsResult(Double score, String parentcategoryname, String term, String definition, String categoryname, Integer id, String category, String parentcategory) {
        this.score = score;
        this.parentcategoryname = parentcategoryname;
        this.term = term;
        this.definition = definition;
        this.categoryname = categoryname;
        this.id = id;
        this.category = category;
        this.parentcategory = parentcategory;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getParentcategoryname() {
        return parentcategoryname;
    }

    public void setParentcategoryname(String parentcategoryname) {
        this.parentcategoryname = parentcategoryname;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentcategory() {
        return parentcategory;
    }

    public void setParentcategory(String parentcategory) {
        this.parentcategory = parentcategory;
    }
}
