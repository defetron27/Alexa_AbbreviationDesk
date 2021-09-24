import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class AbDeskRequestStreamHandler extends SpeechletRequestStreamHandler
{
    private static final Set<String> supportedApplicationIds;

    static
    {
        supportedApplicationIds = new HashSet<>();

        supportedApplicationIds.add("amzn1.ask.skill.796b9b5a-f354-4469-b980-f3418b93b578");
    }

    public AbDeskRequestStreamHandler()
    {
        super(new AbDeskSpeechLet(), supportedApplicationIds);
    }
}
