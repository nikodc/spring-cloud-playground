package io.github.nikodc.webapp;

public class WebAppResponse {

    private ServiceResponse serviceResponse;

    private String webappIp;

    public ServiceResponse getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public String getWebappIp() {
        return webappIp;
    }

    public void setWebappIp(String webappIp) {
        this.webappIp = webappIp;
    }

    @Override
    public String toString() {
        return "WebAppResponse{" +
                "serviceResponse=" + serviceResponse +
                ", webappIp='" + webappIp + '\'' +
                '}';
    }
}
