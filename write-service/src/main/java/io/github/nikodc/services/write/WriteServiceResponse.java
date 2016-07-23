package io.github.nikodc.services.write;

import java.util.ArrayList;
import java.util.List;

public class WriteServiceResponse {

    private List<Item> items;

    private String readServiceIp;

    private String storageServiceIp;

    public WriteServiceResponse() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getReadServiceIp() {
        return readServiceIp;
    }

    public void setReadServiceIp(String readServiceIp) {
        this.readServiceIp = readServiceIp;
    }

    public String getStorageServiceIp() {
        return storageServiceIp;
    }

    public void setStorageServiceIp(String storageServiceIp) {
        this.storageServiceIp = storageServiceIp;
    }

    @Override
    public String toString() {
        return "WriteServiceResponse{" +
                "items=" + items +
                ", readServiceIp='" + readServiceIp + '\'' +
                ", storageServiceIp='" + storageServiceIp + '\'' +
                '}';
    }
}
