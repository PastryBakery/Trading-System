package phase2.trade.repository;

import phase2.trade.logger.LoggerFactory;

import java.io.File;
import java.util.logging.Logger;

/**
 * The implementation of list related operations in {@link Repository}.
 *
 * @param <T> The entity type to be used
 * @author Dan Lyu
 */
public abstract class RepositorySaveImpl<T extends UniqueId> extends RepositoryListImpl<T> implements Savable {

    /**
     * The file object this Repository reads to and saves from
     */
    final File file;

    /**
     * @param path     the path to the file
     * @param saveHook the repository will be saved by a saveHook
     */
    public RepositorySaveImpl(String path, SaveHook saveHook) {
        this.file = new File(path);
        saveHook.addSavable(this);
        mkdirs();
    }

    /**
     * make this file's parent directories
     */
    private void mkdirs() {
        if (!file.exists()) {
            boolean mkdir = new File(file.getParent()).mkdirs();
        }
    }

}
