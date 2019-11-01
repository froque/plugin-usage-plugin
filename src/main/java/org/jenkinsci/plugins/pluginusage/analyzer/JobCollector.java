package org.jenkinsci.plugins.pluginusage.analyzer;

import hudson.PluginWrapper;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jenkins.model.Jenkins;

import org.jenkinsci.plugins.pluginusage.JobsPerPlugin;

public class JobCollector {
	
	private ArrayList<JobAnalyzer> analysers = new ArrayList<>();
	
	public JobCollector() {
		analysers.add(new BuilderJobAnalyzer());
		analysers.add(new BuildWrapperJobAnalyzer());
		analysers.add(new PropertiesJobAnalyzer());
		analysers.add(new PublisherJobAnalyzer());
		analysers.add(new SCMJobAnalyzer());
		analysers.add(new TriggerJobAnalyzer());
	}

	public Map<PluginWrapper, JobsPerPlugin> getJobsPerPlugin()
	{
		Map<PluginWrapper, JobsPerPlugin> mapJobsPerPlugin = new HashMap<>();

		List<AbstractProject> allItems = Jenkins.get().getAllItems(AbstractProject.class);
		
		for(AbstractProject item: allItems)
		{
			for(JobAnalyzer analyser: analysers)
			{
				analyser.doJobAnalyze(item, mapJobsPerPlugin);
			}
		}

		return mapJobsPerPlugin;
	}
	
	public int getNumberOfJobs() {
		List<AbstractProject> allItems = Jenkins.get().getAllItems(AbstractProject.class);
		return allItems.size();	
	}


    public List<PluginWrapper> getOtherPlugins() {
		List<PluginWrapper> allPlugins = Jenkins.get().getPluginManager().getPlugins();
		List<PluginWrapper> others = new ArrayList<>(allPlugins);

		for(JobAnalyzer analyser: analysers)
		{
			others.removeAll(analyser.getPlugins());
		}

		return others;
    }
}
