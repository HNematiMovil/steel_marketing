package ir.hadinemati.steelmarketing.Views.Interfaces;

import java.util.List;

public interface ICallerInformationView {

    void OnProductsListGenerated(List<String> productsList);

    void OnPotentialCallAdded();

}
