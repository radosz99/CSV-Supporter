package classes;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LocalizedBinding {

    // property representing the current locale:
    private final ObjectProperty<Locale> locale ;

    // private property to hold the resource bundle:
    private final ObjectProperty<ResourceBundle> bundle ;

    public LocalizedBinding(String bundleName, Locale locale) {

        this.locale = new SimpleObjectProperty<>(locale);
        this.bundle = new SimpleObjectProperty<>();

        // update resource bundle whenever locale changes:
        bundle.bind(Bindings.createObjectBinding(() -> {
                    Locale l = this.locale.get();
                    if (l == null) {
                        return null ;
                    } else {
                        ResourceBundle resources = ResourceBundle.getBundle(bundleName, l);
                        return resources;
                    }
                },
                this.locale));
    }

    // creates a StringBinding whose value is obtained from the current
    // resource bundle using the provided key. The binding will automatically
    // update if the locale changes:

    public StringBinding createStringBinding(String key) {
        return new StringBinding() {

            {
                bind(bundle);
            }

            @Override
            protected String computeValue() {
                ResourceBundle resources = bundle.get();
                if (resources == null) {
                    return key ;
                } else {
                    return resources.getString(key);
                }
            }

        };
    }


    public String getString(String key) {
        ResourceBundle resources = bundle.get();
        if (resources == null) {
            return key ;
        } else {
            return resources.getString(key);
        }
    }


    // Property accessors for locale:

    public final ObjectProperty<Locale> localeProperty() {
        return this.locale;
    }

    public final java.util.Locale getLocale() {
        return this.localeProperty().get();
    }

    public final void setLocale(final java.util.Locale locale) {
        this.localeProperty().set(locale);
    }
}