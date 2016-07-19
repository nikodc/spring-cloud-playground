package io.github.nikodc.webapp;

public class ServiceResponse {

    private Long requestId;

    private String serviceIp;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "requestId=" + requestId +
                ", serviceIp='" + serviceIp + '\'' +
                '}';
    }
}
