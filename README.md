# Script listener plugin for Nexus 3
## How it works
Plugin listens to ScriptCreatedEvent(OrientDB) [ScriptAPI](https://help.sonatype.com/repomanager3/integrations/rest-and-integration-api/script-api), if created script-Object has
type "listener" then listener tries to evaluate a script content and add as a listener to 
the EventBus. If script-Object will be deleted from DB via [ScriptAPI](https://help.sonatype.com/repomanager3/integrations/rest-and-integration-api/script-api) then it will be removed from 
the EventBus.
### Example of a script
```
import org.sonatype.nexus.repository.storage.AssetEvent;
import org.sonatype.nexus.repository.storage.Asset;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonatype.script.listener.plugin.listeners.AScriptListener;
/**
*  AssetEvent is an OrientDB event 
*/
public сlass AssetEventListener extends AScriptListener {

    private final Logger log = LoggerFactory.getLogger(AssetEventListener.class);
    
	@Subscribe
    public void on(AssetEvent event) {
	    log.info("SCRIPT: event class => {} ", event.getClass().getCanonicalName());
		log.info("SCRIPT: asset => {}" event.getAsset().name()); 
	}
}

return new AssetEventListener();
```

