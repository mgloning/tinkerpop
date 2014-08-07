package com.tinkerpop.gremlin.console.plugin

import com.tinkerpop.gremlin.groovy.plugin.GremlinPlugin
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
class PluggedIn {
    private final GremlinPlugin plugin
    private boolean activated = false

    public PluggedIn(final GremlinPlugin plugin, final boolean activated) {
        this.plugin = plugin
        this.activated = activated
    }

    GremlinPlugin getPlugin() {
        return plugin
    }

    boolean getActivated() {
        return activated
    }

    void activate(final Groovysh shell, final IO io) {
        plugin.pluginTo(new ConsolePluginAcceptor(shell, io))
        this.activated = true
    }

    void deactivate() {
        this.activated = false
    }
}
