package com.f2pstarhunt.stars.starminers;

import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Client which fetches world information from Jagex.
 */
// implementation adapted from WorldService in the api.runelite.net project
@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class WorldClient {

    private static final String URL = "http://www.runescape.com/g=oldscape/slr.ws?order=LPWM";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Set<Integer> fetchF2pWorlds() {
        try {
            HttpResponse<byte[]> response = httpClient.send(HttpRequest.newBuilder().uri(URI.create(URL)).build(), HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == HTTP_OK) {
                byte[] bytes = response.body();

                Set<Integer> result = new HashSet<>();

                ByteBuffer buffer = ByteBuffer.wrap(bytes);

                int length = buffer.getInt();
                buffer.limit(length + 4); // the length + size of the first 'length' int.

                int worldCount = buffer.getShort() & 0xFFFF;

                for (int i = 0; i < worldCount; i += 1) {
                    int worldNumber = buffer.getShort() & 0xFFFF;
                    int worldTypes = buffer.getInt();
                    String address = readNullTerminatedString(buffer);
                    String activity = readNullTerminatedString(buffer);
                    int location = buffer.get() & 0xFF;
                    short playerCount = buffer.getShort();

                    if (isF2p(worldTypes)) {
                        result.add(worldNumber);
                    }
                }

                return result;
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Could not fetch world list from Jagex, message: " + e.getMessage());
            // no need to log stack trace
        }
        return Collections.emptySet();
    }

    // see ServiceWorldType
    private static boolean isF2p(int worldTypes) {
        return (worldTypes & 1) == 0;
    }

    private static String readNullTerminatedString(ByteBuffer buffer) {
        int startPos = buffer.position();

        byte b;
        do {
            b = buffer.get();
        } while (b != 0);

        int endPos = buffer.position();
        
        return new String(buffer.slice(startPos, endPos - startPos).array(), StandardCharsets.ISO_8859_1);
    }
}
