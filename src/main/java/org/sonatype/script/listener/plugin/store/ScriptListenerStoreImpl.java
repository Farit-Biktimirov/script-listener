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

import static com.google.common.collect.Lists.newArrayList;

@Named("mybatis")
@Singleton
public class ScriptListenerStoreImpl extends ConfigStoreSupport<ScriptListenerDAO> implements ScriptListenerStore {

    @Inject
    public ScriptListenerStoreImpl(DataSessionSupplier sessionSupplier) {
        super(sessionSupplier);
    }


    @Override
    @Transactional
    public List<ScriptListenerData> list() {
        List<ScriptListenerData> list = newArrayList(dao().browse().orElse(null));
        return ImmutableList.copyOf(list);
    }

    @Override
    @Transactional
    public ScriptListenerData create(final ScriptListenerData data) throws  ValidationErrorsException {
        try {
            dao().create(data);
            return data;
        }
        catch (Exception e) {
            throw new ValidationErrorsException("A couple with the same combination of EventType and ScriptName already exists.", e);
        }
    }

    @Override
    @Transactional
    public List<ScriptListenerData> readByEventType(final String name) {
        return ImmutableList.copyOf(dao().readByEventType(name).orElse(null));
    }

    @Override
    @Transactional
    public List<String> getScriptsNamesByEventType(final String eventType) {
        return ImmutableList.copyOf(dao().getScriptsNamesByEventType(eventType).orElse(null));
    }

    @Override
    @Transactional
    public boolean deleteByScriptName(final String scriptName) {
        return dao().deleteByScriptName(scriptName);
    }

}
