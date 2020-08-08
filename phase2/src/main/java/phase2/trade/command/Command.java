package phase2.trade.command;

import org.hibernate.annotations.Cascade;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Command<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    boolean ifUndone = false;

    private Long timestamp;

    private Long undoTimestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    Collection<Long> effectedIds;

    protected transient GatewayBundle gatewayBundle;

    public Command(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
        this.effectedIds = new HashSet<>();
    }

    public Command() {

    }


    public abstract void execute(Callback<T> callback, String... args);

    public abstract void undo();

    public abstract void redo(); // It seems we don't need to implement redo. Also redo may mess up the uid

    public abstract Class<?> getClassToOperateOn();

    protected void save() {
        timestamp = System.currentTimeMillis();
        gatewayBundle.getCommandGateway().submitTransaction(() -> gatewayBundle.getCommandGateway().add(getThis()));
    }

    protected void updateUndo() {
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        gatewayBundle.getCommandGateway().submitTransaction(() -> gatewayBundle.getCommandGateway().update(getThis()));
    }

    public Command<T> getThis() {
        return this;
    }

    public void setGatewayBundle(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public void addEffectedId(Long id) {
        effectedIds.add(id);
    }

    public Collection<Long> getEffectedIds() {
        return effectedIds;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public abstract CRUDType getCRUDType();

    public Long getTimestamp() {
        return timestamp;
    }

    public void isUndoable(Callback<List<Command<?>>> callback) {
        gatewayBundle.getCommandGateway().submitSession(() -> callback.call(gatewayBundle.getCommandGateway().isUndoable(effectedIds, timestamp)));
    }
}