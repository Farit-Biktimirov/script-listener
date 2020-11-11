package org.sonatype.script.listener.plugin.dao;


import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.common.entity.HasEntityId;

public class ScriptListenerData implements HasEntityId {

    private EntityId id;
    private String scriptname;
    private String eventtype;

    @Override
    public EntityId getId() {
        return null;
    }

    @Override
    public void setId(EntityId entityId) {

    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
            return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (id != null) {
            return id.equals(((ScriptListenerData) obj).getId());
        }
        return super.equals(obj);
    }
}
