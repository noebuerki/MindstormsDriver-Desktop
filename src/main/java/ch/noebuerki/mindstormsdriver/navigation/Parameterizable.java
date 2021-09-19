package ch.bbcag.mdriver.navigation;

import ch.bbcag.mdriver.navigation.parameters.BaseParams;

public interface Parameterizable<T extends BaseParams> {
    void onNavigate(T params);
}
