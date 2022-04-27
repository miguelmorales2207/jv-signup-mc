package co.com.dk.juanvaldez.jvsignupmc.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class GeneralConfig {

    @Value("${web.client.timeout}")
    private int webClientTimeOut;

    @Value("${web.client.buffer.size}")
    private int bufferSize;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientTimeOut)
            .responseTimeout(Duration.ofMillis(webClientTimeOut))
            .doOnConnected(connection ->
                connection.addHandlerLast(
                    new ReadTimeoutHandler(webClientTimeOut, TimeUnit.MILLISECONDS))
                    .addHandlerLast(
                        new WriteTimeoutHandler(webClientTimeOut, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(x -> x.defaultCodecs().maxInMemorySize(bufferSize)).build())
            .build();
    }

}
