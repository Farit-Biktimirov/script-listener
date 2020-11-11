package org.sonatype.script.listener.plugin.dao;

public class ScriptListenerData{

    private String scriptname;
    private String eventtype;

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
       String concat = this.eventtype.concat(this.scriptname);
       return concat.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return scriptname.equals(((ScriptListenerData) obj).getScriptname()) &&
                    eventtype.equals(((ScriptListenerData) obj).getEventtype());
    }
}
