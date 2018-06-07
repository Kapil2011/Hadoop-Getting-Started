package com.kapildas.afpvalidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kapildas.afpvalidator.config.ApplicationConfiguration;
import com.kapildas.afpvalidator.validator.CorpIdValidator;
import com.kapildas.afpvalidator.validator.DuplicateValidator;
import com.kapildas.afpvalidator.validator.Validator;

public class AFPFile {
    private final String corpId;
    private final LocalDate cycleDate;
    private final int cycleNumber;
    private final String shortCode;
    private final String jobName;
    private final String fileFormat;
    private final String fileType;
    private Path originalFilePath;
    private Path destinationFileName;


    public AFPFile(Factory factory) {
        this.shortCode = factory.shortCode;
        this.cycleNumber = factory.cycleNumber;
        this.corpId = factory.corpId;
        this.cycleDate = factory.cycleDate;
        this.fileFormat = factory.fileFormat;
        this.fileType = factory.fileType;
        this.jobName = factory.jobName;
    }


    public String getCorpId() {
        return corpId;
    }

    public LocalDate getCycleDate() {
        return cycleDate;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getJobName() {
        return jobName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public String getFileType() {
        return fileType;
    }

    public Path getOriginalFilePath() {
        return originalFilePath;
    }

    public void rename() throws IOException {
        if (originalFilePath != null && destinationFileName!=null) {
            Files.move(originalFilePath, destinationFileName, StandardCopyOption.COPY_ATTRIBUTES);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ Corp ID = ").append(corpId);
        builder.append(", Cycle Date = ").append(cycleDate.toString());
        builder.append(", Cycle Number = ").append(cycleNumber);
        builder.append(", Short Code = ").append(shortCode);
        builder.append(", Job Name = ").append(jobName);
        builder.append(", File Format = ").append(fileFormat);
        builder.append(", File Type = ").append(fileType);
        return builder.append(" }").toString();
    }

    public void validate() {
        Validator[] validators = {new CorpIdValidator(), new DuplicateValidator()};
        boolean isValid = true;
        String errorMessage = null;
        for (Validator validator : validators) {
            validator.validate(this);
            isValid = validator.isValid();
            if (!isValid) {
                StringJoiner joiner = new StringJoiner("\n");
                validator.getErrors().forEach(message -> joiner.add(message));
                errorMessage = joiner.toString();
                break;
            }
        }
    }

    public static final class Factory {
        private String corpId;
        private LocalDate cycleDate;
        private int cycleNumber;
        private String shortCode;
        private String jobName;
        private String fileFormat;
        private String fileType;

        public Factory setCorpId(String corpId) {
            this.corpId = corpId;
            return this;
        }

        public Factory setCycleDate(LocalDate cycleDate) {
            this.cycleDate = cycleDate;
            return this;
        }

        public Factory setCycleNumber(int cycleNumber) {
            this.cycleNumber = cycleNumber;
            return this;
        }

        public Factory setShortCode(String shortCode) {
            this.shortCode = shortCode;
            return this;
        }

        public Factory setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Factory setFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public Factory setFileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public AFPFile build() {
            return new AFPFile(this);
        }

        public static AFPFile build(String filePath) {
            final Path afpFilePath = Paths.get(filePath);
            final Pattern pattern = Pattern.compile(ApplicationConfiguration.FILE_NAME_PATTERN.getValue());
            final Matcher matcher = pattern.matcher(afpFilePath.getFileName().toString());
            if (!matcher.matches()) {
                throw new ValidationException("The file name doesnt match the pattern");
            }
            Factory factory = new Factory();
            factory.setCorpId(matcher.group("corpId"));
            factory.setCycleDate(LocalDate.parse(matcher.group("cycleDate"), DateTimeFormatter.BASIC_ISO_DATE));
            factory.setCycleNumber(Integer.parseInt(matcher.group("cycleNumber")));
            factory.setShortCode(matcher.group("shortCode"));
            factory.setJobName(matcher.group("jobName"));
            factory.setFileFormat(matcher.group("fileFormat"));
            factory.setFileType(matcher.group("fileType"));
            final AFPFile afpFile = new AFPFile(factory);
            afpFile.originalFilePath = afpFilePath;
            afpFile.destinationFileName = Paths.get(afpFilePath.getParent().toString(), matcher.group("destFile"));
            return afpFile;
        }
    }
}
