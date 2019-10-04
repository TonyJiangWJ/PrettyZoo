package cc.cc1234.main.view;

import cc.cc1234.main.cache.TreeViewCache;
import cc.cc1234.main.controller.TreeNodeMenuViewController;
import cc.cc1234.main.controller.VToast;
import cc.cc1234.main.model.ZkNode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTreeCell extends TreeCell<ZkNode> {

    private static final Logger log = LoggerFactory.getLogger(DefaultTreeCell.class);

    private final Stage primaryStage;

    public DefaultTreeCell(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    protected void updateItem(ZkNode item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.setOnMouseClicked(this::mouseEvent);
            final TreeItem<ZkNode> treeItem = getTreeItem();
            final Text graphic = new Text(item.getName());
            // ephemeral node
            if (treeItem.getValue().getEphemeralOwner() != 0) {
                graphic.setFill(Color.valueOf("#ffab00"));
                setGraphic(graphic);
                setText(null);
            } else {
                setText(treeItem.getValue().getName());
            }
        }
    }

    private void mouseEvent(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            final TreeViewCache<ZkNode> cache = TreeViewCache.getInstance();
            final TreeView<ZkNode> treeView = cache.getTreeView();
            final Window window = treeView.getParent().getScene().getWindow();
            final double x = event.getScreenX() - event.getX();
            try {
                TreeNodeMenuViewController.show(window, getTreeItem(), x, event.getScreenY(), treeView.getWidth());
            } catch (Exception e) {
                log.error("tree node menu show failed", e);
                VToast.toastFailure(primaryStage, "unknown error");
            }
        }
    }

}
