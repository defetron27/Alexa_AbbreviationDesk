package Models;

public class AbbreResponse
{
    private AbbreResults results;

    public AbbreResponse() {
    }

    public AbbreResponse(AbbreResults results) {
        this.results = results;
    }


    public AbbreResults getResults() {
        return results;
    }

    public void setResults(AbbreResults results) {
        this.results = results;
    }
}
