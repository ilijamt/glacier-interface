package com.matoski.glacier.base;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

/**
 * A generic abstract command used as a base for all the commands related to Amazon Glacier.
 *
 * @param <T> usually a command that extends from {@link GenericCommand}
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public abstract class AbstractCommand<T> extends AbstractEmptyCommand<T> {

    /**
     * Service name.
     */
    public static final String SERVICE_NAME = "glacier";
    /**
     * Service name for SNS.
     */
    public static final String SERVICE_SNS_NAME = "sns";
    /**
     * Service name for SQS.
     */
    public static final String SERVICE_SQS_NAME = "sqs";
    /**
     * Does this service has HTTP endpoint.
     */
    public Boolean hasHttpEndpoint = false;
    /**
     * Does this servie has HTTPS endpoint.
     */
    public Boolean hasHttpsEndpoint = false;
    /**
     * The service endpoint.
     */
    public transient String serviceEndpoint = null;
    /**
     * Basic AWS credentials.
     */
    protected BasicAWSCredentials credentials = null;
    /**
     * The client used to connect to amazon glacier.
     */
    protected AmazonGlacierClient client = null;
    /**
     * The region used for all operations with Amazon Glacier.
     */
    protected Region region = null;
    /**
     * Default protocol
     */
    protected Protocol protocol = Protocol.HTTPS;

    /**
     * Constructor.
     *
     * @param config  The configuration for the system
     * @param command The command that we will process
     * @throws VaultNameNotPresentException No vault present in the config
     * @throws RegionNotSupportedException  Region is invalid
     */
    public AbstractCommand(Config config, T command) throws VaultNameNotPresentException,
            RegionNotSupportedException {
        super(config, command);

        this.setProtocol();
        this.region = Region.getRegion(Regions.fromName(config.getRegion()));
        this.credentials = new BasicAWSCredentials(config.getKey(), config.getSecretKey());

        this.client = new AmazonGlacierClient(this.credentials, this.generateConfiguration());

        if (!this.region.isServiceSupported(SERVICE_NAME)) {
            throw new RegionNotSupportedException();
        }

        this.hasHttpEndpoint = this.region.hasHttpEndpoint(SERVICE_NAME);
        this.hasHttpsEndpoint = this.region.hasHttpsEndpoint(SERVICE_NAME);
        this.serviceEndpoint = this.region.getServiceEndpoint(SERVICE_NAME);

        this.client.setRegion(this.region);
        this.client.setEndpoint(this.serviceEndpoint);

    }

    public void setProtocol() {
    }

    public ClientConfiguration generateConfiguration() {
        final ClientConfiguration configuration = new ClientConfiguration();
        configuration.setConnectionTimeout(70 * 1000);
        configuration.setProtocol(this.protocol);
        return configuration;
    }

}
