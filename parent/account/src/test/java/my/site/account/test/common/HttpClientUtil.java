package my.site.account.test.common;


import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;


public class HttpClientUtil {

    private static Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private RestTemplate restTemplate = createRestTemplate();
    private static final String DEF_HOST = "http://127.0.0.1:%s/site";
    private String host = "http://127.0.0.1:8080/site";

    public void setHost(String host){
        this.host = host;
    }

    public void setPortWithDefHost(String port){
        this.host = String.format(DEF_HOST, port);
    }


    public RestTemplate get(){
        return restTemplate;
    }

    public <T> T getPType(ParameterizedTypeReference<T> typeRef, String url){
        ResponseEntity<T> responseEntity = restTemplate.exchange(host + url, HttpMethod.GET, null, typeRef);
        return responseEntity.getBody();
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = null;
        try {
            //SSL证书忽略
            TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            restTemplate = new RestTemplate(requestFactory);
            //multipart编码问题？
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            formHttpMessageConverter.setMultipartCharset(StandardCharsets.UTF_8);
            restTemplate.getMessageConverters().add(0, formHttpMessageConverter);
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
        return restTemplate;
    }

}
