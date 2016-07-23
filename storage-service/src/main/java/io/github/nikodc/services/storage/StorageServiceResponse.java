
package io.github.nikodc.services.storage;

import java.util.ArrayList;
import java.util.List;

public class StorageServiceResponse {

    private List<Item> items;

    private String storageServiceIp;

    public StorageServiceResponse() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getStorageServiceIp() {
        return storageServiceIp;
    }

    public void setStorageServiceIp(String storageServiceIp) {
        this.storageServiceIp = storageServiceIp;
    }

    @Override
    public String toString() {
        return "StorageServiceResponse{" +
                "items=" + items +
                ", storageServiceIp='" + storageServiceIp + '\'' +
                '}';
    }
}
