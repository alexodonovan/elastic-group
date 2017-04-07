package sample.data.elasticsearch;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticConfig {

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
		NodeBuilder nodeBuilder = nodeBuilder();

		Settings settings = nodeBuilder	.getSettings()
										.put("path.home", "/tmp/elastic")
										.put("path.data", "/tmp/elastic/data/elasticsearch/nodes/0")
										.put("http.enabled", true)
										.build();

		return new ElasticsearchTemplate(nodeBuilder()	.local(true)
														.settings(settings)
														.node()
														.client());
	}

	private NodeBuilder nodeBuilder() {
		return new NodeBuilder();
	}

}
