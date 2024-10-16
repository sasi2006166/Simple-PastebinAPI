package me.candiesjar.builders;

import lombok.SneakyThrows;
import me.candiesjar.objects.CodeType;
import me.candiesjar.objects.Expiration;
import me.candiesjar.objects.Privacy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Pastebin {

    private final String devKey;
    private final List<String> text;
    private final Privacy privacy;
    private final String name;
    private final Expiration expiration;
    private final CodeType codeType;

    public Pastebin(String devKey, List<String> text, Privacy privacy, String name, Expiration expiration, CodeType codeType) {
        this.devKey = devKey;
        this.text = text;
        this.privacy = privacy;
        this.name = name;
        this.expiration = expiration;
        this.codeType = codeType;
    }

    public String toString() {
        return "Pastebin(devKey=" + this.devKey + ", text=" + this.text + ", privacy=" + this.privacy + ", name=" + this.name + ", expiration=" + this.expiration + ", codeType=" + this.codeType + ")";
    }

    @SneakyThrows
    public String send() {
        StringBuilder apiBuilder = new StringBuilder("api_dev_key=")
                .append(URLEncoder.encode(this.devKey, "UTF-8"))
                .append("&api_option=paste");

        StringBuilder codeBuilder = new StringBuilder();
        for (String line : this.text) {
            codeBuilder.append(line).append("\n");
        }
        apiBuilder.append("&api_paste_code=")
                .append(URLEncoder.encode(codeBuilder.toString(), "UTF-8"));

        if (this.privacy != null) {
            apiBuilder.append("&api_paste_private=").append(this.privacy.getPrivacy());
        }

        if (this.name != null) {
            apiBuilder.append("&api_paste_name=").append(URLEncoder.encode(this.name, "UTF-8"));
        }

        if (this.expiration != null) {
            apiBuilder.append("&api_paste_expire_date=").append(this.expiration.getExpiration());
        }

        if (this.codeType != null) {
            apiBuilder.append("&api_paste_format=").append(this.codeType.getName());
        }

        String postParams = apiBuilder.toString();

        URL url = new URL("https://pastebin.com/api/api_post.php");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(postParams.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            return "Failed to send the request. HTTP response code: " + responseCode;
        }

        InputStream inputStream = connection.getInputStream();
        StringBuilder response = new StringBuilder();
        int i;

        while ((i = inputStream.read()) != -1) {
            response.append((char) i);
        }

        return response.toString();
    }

    public static class PastebinBuilder {
        private String devKey;
        private List<String> text;
        private Privacy privacy;
        private String name;
        private Expiration expiration;
        private CodeType codeType;

        public PastebinBuilder() {
        }

        public PastebinBuilder setDevKey(String devKey) {
            this.devKey = devKey;
            return this;
        }

        public PastebinBuilder setText(List<String> text) {
            this.text = text;
            return this;
        }

        public PastebinBuilder setPrivacy(Privacy privacy) {
            this.privacy = privacy;
            return this;
        }

        public PastebinBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PastebinBuilder setExpiration(Expiration expiration) {
            this.expiration = expiration;
            return this;
        }

        public PastebinBuilder setCodeType(CodeType codeType) {
            this.codeType = codeType;
            return this;
        }

        public Pastebin build() {
            return new Pastebin(this.devKey, this.text, this.privacy, this.name, this.expiration, this.codeType);
        }

    }

}
