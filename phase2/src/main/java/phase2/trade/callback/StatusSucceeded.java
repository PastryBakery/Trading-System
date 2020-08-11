package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public class StatusSucceeded extends ResultStatus {

    @Override
    public void handle() {
        run(new Runnable() {
            @Override
            public void run() {

                succeeded.run();
                after.run();
            }
        });
    }


    @Override
    public boolean ifPass() {
        return true;
    }
}
