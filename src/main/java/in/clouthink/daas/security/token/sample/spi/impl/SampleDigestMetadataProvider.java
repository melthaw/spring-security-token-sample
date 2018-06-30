package in.clouthink.daas.security.token.sample.spi.impl;

import in.clouthink.daas.security.token.spi.DigestMetadataProvider;

public class SampleDigestMetadataProvider implements DigestMetadataProvider<SampleUser> {

    @Override
    public String getDigestAlgorithm(SampleUser user) {
        return user.getPasswordDigestMethod();
    }

    @Override
    public String getSalt(SampleUser user) {
        return user.getSalt();
    }

}
