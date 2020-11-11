package org.sonatype.script.listener.plugin.store;


import org.sonatype.nexus.common.entity.EntityUUID;
import org.sonatype.nexus.rest.ValidationErrorsException;
import org.sonatype.script.listener.plugin.dao.ScriptListenerDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.datastore.ConfigStoreSupport;
import org.sonatype.nexus.datastore.api.DataSessionSupplier;
import org.sonatype.nexus.transaction.Transactional;

import com.google.common.collect.ImmutableList;
import org.sonatype.script.listener.plugin.dao.ScriptListenerData;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

@Named("mybatis")
@Singleton
public class ScriptListenerStore extends ConfigStoreSupport<ScriptListenerDAO> {

    @Inject
    public ScriptListenerStore(DataSessionSupplier sessionSupplier) {
        super(sessionSupplier);
    }


    @Transactional
    public List<ScriptListenerData> list() {
        return ImmutableList.copyOf(dao().browse());
    }

    @Transactional
    public ScriptListenerData create(final ScriptListenerData data) {
        try {
            if (null != data ) {
                if (null == data.getId()) {
                    data.setId(new EntityUUID(UUID.randomUUID()));
                }
                dao().create(data);
                return data;
            }
            return null;
        }
        catch (Exception e) {
            throw new ValidationErrorsException("Not Unique", "A couple with the same combination of EventType and ScriptName already exists.");
        }
    }

    @Transactional
    public ScriptListenerData getById(final String id) {
        return dao().read(new EntityUUID(fromString(id))).orElse(null);
    }

    @Transactional
    public List<ScriptListenerData> readByEventType(final String name) {
        return ImmutableList.copyOf(dao().readByEventType(name).orElse(null));
    }

    @Transactional
    public List<String> getScriptsNamesByEventType(final String eventType) {
        return ImmutableList.copyOf(dao().getScriptsNamesByEventType(eventType).orElse(null));
    }


    @Transactional
    public boolean deleteByScriptName(final String scriptName) {
        return dao().deleteByScriptName(scriptName);
    }

    @Transactional
    public void delete(final String id) {
        dao().delete(new EntityUUID(fromString(id)));
    }
}
