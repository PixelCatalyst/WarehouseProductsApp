package com.pixcat.warehouseproducts;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.io.InputStream;

@Getter
public class ImagePayload {

    private final static Tika tika = new Tika();
    private final static String IMAGE_TYPE = "image";

    private final byte[] data;
    private final MediaType mediaType;

    public static ImagePayload of(InputStream dataStream, String contentTypeHint) {
        return new ImagePayload(dataStream, contentTypeHint);
    }

    private ImagePayload(InputStream dataStream, String contentTypeHint) {
        try {
            this.data = IOUtils.toByteArray(dataStream);

            String detectedType = tika.detect(TikaInputStream.get(data), buildMetadata(contentTypeHint));
            mediaType = MediaType.parse(detectedType);
            if (!mediaType.getType().equals(IMAGE_TYPE)) {
                throw new IllegalArgumentException("Invalid image content type");
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to parse image data", ex);
        }
    }

    private Metadata buildMetadata(String contentTypeHint) {
        Metadata metadata = new Metadata();
        if (mediaType != null) {
            metadata.set(Metadata.CONTENT_TYPE, contentTypeHint);
        }
        return metadata;
    }
}
