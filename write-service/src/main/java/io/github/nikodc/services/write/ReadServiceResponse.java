package io.github.nikodc.services.write;

import java.util.ArrayList;
import java.util.List;

public class ReadServiceResponse {

    private List<Item> items;

    private String readServiceIp;

    private String storageServiceIp;

    public ReadServiceResponse() {
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
        return "ReadServiceResponse{" +
                "items=" + items +
                ", readServiceIp='" + readServiceIp + '\'' +
                ", storageServiceIp='" + storageServiceIp + '\'' +
                '}';
    }
}
