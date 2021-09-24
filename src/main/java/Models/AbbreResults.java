package Models;

import java.util.List;

public class AbbreResults
{
    private List<AbbreResultsResult> result;

    public AbbreResults() {
    }

    public AbbreResults(List<AbbreResultsResult> result) {
        this.result = result;
    }


    public List<AbbreResultsResult> getResult() {
        return result;
    }

    public void setResult(List<AbbreResultsResult> result) {
        this.result = result;
    }
}
