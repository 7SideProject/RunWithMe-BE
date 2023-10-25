package com.runwithme.runwithme.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Value("${cloud.aws.s3.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.s3.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.s3.region.static}")
	private String region;

	@Value("${cloud.aws.s3.endpoint}")
	private String endpoint;

	@Bean
	public AmazonS3 build() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		return AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
			.build();
	}
}
