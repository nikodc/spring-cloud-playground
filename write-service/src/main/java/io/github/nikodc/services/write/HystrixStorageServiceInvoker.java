package io.github.nikodc.services.write;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@ConditionalOnConsulEnabled
public class HystrixStorageServiceInvoker implements StorageServiceInvoker {

    private static final Logger log = LoggerFactory.getLogger(HystrixStorageServiceInvoker.class);

    @Autowired
    DirectStorageServiceInvoker directStorageServiceInvoker;

    @Value("${application.hystrixCommandName}")
    private String hystrixCommandName;

    @Override
    public StorageServiceResponse addItem(String db, Item item) {
        return new StorageServiceCommand(db, item).execute();
    }

    private String getConcreteHystrixCommandName(String db) {
        try {
            return String.format(hystrixCommandName, db);
        } catch (Exception e) {
            log.error("Exception getting concrete hystrix command name, hystrixCommandName: {}, db: {}",
                    hystrixCommandName, db);
            throw new RuntimeException(e);
        }
    }

    public class StorageServiceCommand extends HystrixCommand<StorageServiceResponse> {

        private final String db;

        private final Item item;

        public StorageServiceCommand(String db, Item item) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PlaygroundGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(getConcreteHystrixCommandName(db))));
            this.db = db;
            this.item = item;
        }

        @Override
        protected StorageServiceResponse run() {
            return directStorageServiceInvoker.addItem(db, item);
        }
    }

}
