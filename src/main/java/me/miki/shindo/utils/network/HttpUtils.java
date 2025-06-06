package me.miki.shindo.utils.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.miki.shindo.logger.ShindoLogger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {

    private static String ACCEPTED_RESPONSE = "application/json";
    private static Gson gson = new Gson();

    public static JsonObject readJson(HttpURLConnection connection) {
        return gson.fromJson(readResponse(connection), JsonObject.class);
    }

    public static JsonObject postJson(String url, Object request) {
        try {
            HttpURLConnection connection = setupConnection(url, UserAgents.MOZILLA, 5000, false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", ACCEPTED_RESPONSE);
            connection.setRequestProperty("Accept", ACCEPTED_RESPONSE);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(gson.toJson(request).getBytes(StandardCharsets.UTF_8));
            }

            return gson.fromJson(readResponse(connection), JsonObject.class);
        } catch (Exception e) {
            ShindoLogger.error("Failed to post JSON to " + url, e);
            return null;
        }
    }

    public static String get(String url) {
        try {
            HttpURLConnection connection = setupConnection(url, UserAgents.MOZILLA, 5000, false);
            connection.setRequestMethod("GET");
            return readResponse(connection);
        } catch (IOException e) {
            ShindoLogger.error("GET request failed: " + url, e);
            return null;
        }
    }



    public static String readResponse(HttpURLConnection connection) {
        if (connection == null) return null;

        String redirection = connection.getHeaderField("Location");

        if (redirection != null) {
            return readResponse(setupConnection(redirection, UserAgents.MOZILLA, 5000, false));
        }

        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append('\n');
            }
        } catch (IOException e) {
            ShindoLogger.error("Failed to read response", e);
        }

        return response.toString();
    }

    public static JsonObject readJson(String url, Map<String, String> headers, String userAgent) {
        try {
            HttpURLConnection connection = setupConnection(url, userAgent, 5000, false);
            if (connection == null) return null;

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            InputStream is = connection.getResponseCode() != 200 ? connection.getErrorStream() : connection.getInputStream();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return gson.fromJson(readResponse(rd), JsonObject.class);
            }
        } catch (IOException e) {
            ShindoLogger.error("Failed to read json", e);
            return null;
        }
    }

    public static JsonObject readJson(String url, Map<String, String> headers) {
        return readJson(url, headers, UserAgents.MOZILLA);
    }

    private static String readResponse(BufferedReader br) {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            ShindoLogger.error("Failed to read response", e);
            return null;
        }
    }


    public static boolean downloadFile(String url, File outputFile, String userAgent, int timeout, boolean useCaches) {
        url = url.replace(" ", "%20");

        try (FileOutputStream fileOut = new FileOutputStream(outputFile);
             BufferedInputStream in = new BufferedInputStream(setupConnection(url, userAgent, timeout, useCaches).getInputStream())) {
            org.apache.commons.io.IOUtils.copy(in, fileOut);
        } catch (Exception e) {
            ShindoLogger.error("Failed to download file", e);
            return false;
        }

        return true;
    }

    public static boolean downloadFile(String url, File outputFile, String userAgent, int timeout, boolean useCaches, long expectedSize) {
        url = url.replace(" ", "%20");
        File tempFile = new File(outputFile.getAbsolutePath() + ".part");

        try {
            long downloadedLength = 0;
            if (tempFile.exists()) {
                downloadedLength = tempFile.length();
            }

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setUseCaches(useCaches);

            if (downloadedLength > 0) {
                connection.setRequestProperty("Range", "bytes=" + downloadedLength + "-");
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_PARTIAL) {
                ShindoLogger.error("Server returned HTTP code: " + responseCode);
                return false;
            }

            try (InputStream in = connection.getInputStream();
                 RandomAccessFile raf = new RandomAccessFile(tempFile, "rw")) {
                raf.seek(downloadedLength);

                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    raf.write(buffer, 0, read);
                }
            }

            if (tempFile.length() == expectedSize) {
                if (outputFile.exists()) outputFile.delete();
                if (!tempFile.renameTo(outputFile)) {
                    ShindoLogger.error("Failed to rename temp file to final file");
                    return false;
                }
            } else {
                ShindoLogger.error("Download incomplete: expected " + expectedSize + ", but got " + tempFile.length());
                return false;
            }

        } catch (Exception e) {
            ShindoLogger.error("Failed to download file", e);
            return false;
        }

        return true;
    }

    public static boolean downloadFile(String url, File outputFile, String userAgent, long expectedSize) {
        return downloadFile(url, outputFile, userAgent, 5000, false, expectedSize);
    }

    public static boolean downloadFile(String url, File outputFile, long expectedSize) {
        return downloadFile(url, outputFile, UserAgents.MOZILLA, 5000, false, expectedSize);
    }

    public static boolean downloadFile(String url, File outputFile, String userAgent) {
        return downloadFile(url, outputFile, userAgent, 5000, false);
    }

    public static boolean downloadFile(String url, File outputFile) {
        return downloadFile(url, outputFile, UserAgents.MOZILLA, 5000, false);
    }

    public static HttpURLConnection setupConnection(String url, String userAgent, int timeout, boolean useCaches) {
        try {
            url = PunycodeUtils.punycode(url);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(useCaches);
            connection.addRequestProperty("User-Agent", userAgent);
            connection.setRequestProperty("Accept-Language", "en-US");
	        connection.setRequestProperty("Accept-Charset","UTF-8");
	        connection.setReadTimeout(timeout);
	        connection.setConnectTimeout(timeout);
	        connection.setDoOutput(true);
	        
	        return connection;
		} catch (Exception e) {
			ShindoLogger.error("Failed to setup connection");
		}
        
		return null;
	}

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

}
