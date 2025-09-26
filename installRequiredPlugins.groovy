import jenkins.model.Jenkins

def jenkins = Jenkins.instance
def pm = jenkins.pluginManager
def uc = jenkins.updateCenter

// Ensure update site metadata is up-to-date
uc.updateAllSites()

// List of required plugins
def plugins = ["github", "mstest", "workflow-aggregator", "docker-build-publish"]

plugins.each { pluginName ->
    if (!pm.getPlugin(pluginName)) {
        def plugin = uc.getPlugin(pluginName)
        if (plugin) {
            def deployment = plugin.deploy(true)
            deployment.get() // Wait for installation to complete
            println "Installed plugin: ${pluginName}"
        } else {
            println "Plugin ${pluginName} not found in Update Center!"
        }
    } else {
        println "Plugin ${pluginName} already installed."
    }
}

// Restart Jenkins if needed
// jenkins.safeRestart() // safer than .restart()