import Models.AbbreResponse;
import Models.AbbreResults;
import Models.AbbreResultsResult;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.XML;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class AbDeskSpeechLet implements SpeechletV2
{
    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> speechletRequestEnvelope)
    {

    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> speechletRequestEnvelope)
    {
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> speechletRequestEnvelope)
    {
        Intent intent = speechletRequestEnvelope.getRequest().getIntent();

        Session session = speechletRequestEnvelope.getSession();

        String intentName = (intent != null) ? intent.getName() : "";

        if (intentName != null && intentName.equals("GetAbName"))
        {
            if (intent.getSlot("name").getValue() != null)
            {
                String abName = intent.getSlot("name").getValue();

                if (abName != null && !abName.equals("") && !abName.equals("null"))
                {
                    String fallBackSpeechText = "I could not find any abbreviation or acronym for \"" + abName + "\". Please say correct or another abbreviation or acronym name";

                    String fallBackRePromptText = "Please say correct or another abbreviation or acronym name";

                    if (!getResponseForName(abName).equals(""))
                    {
                        String abResponse = getResponseForName(abName);

                        AbbreResponse response = new Gson().fromJson(abResponse,AbbreResponse.class);

                        if (response != null)
                        {
                            AbbreResults abbreResults = response.getResults();

                            if (abbreResults != null)
                            {
                                List<AbbreResultsResult> resultsResult = abbreResults.getResult();

                                if (resultsResult != null && resultsResult.size() > 0)
                                {
                                    if (resultsResult.size() > 50)
                                    {
                                        session.setAttribute("name",abName);
                                        session.setAttribute("limit",String.valueOf(50));
                                        session.setAttribute("start",String.valueOf(0));

                                        List<AbbreResultsResult> newResultsResult = resultsResult.subList(0,50);

                                        StringBuilder stringBuilder = new StringBuilder();

                                        for(int i=0; i<newResultsResult.size(); i++)
                                        {
                                            if (i == newResultsResult.size() - 1)
                                            {
                                                stringBuilder.append(String.valueOf(newResultsResult.size())).append(". ");
                                            }
                                            else
                                            {
                                                stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                            }

                                            AbbreResultsResult result = newResultsResult.get(i);

                                            if (result.getDefinition() != null)
                                            {
                                                stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                            }

                                            if (result.getCategoryname() != null)
                                            {
                                                stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                            }

                                            if (result.getParentcategoryname() != null)
                                            {
                                                stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                            }
                                        }

                                        String speechText = "Abbreviations of " + abName + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + abName ,speechText,rePromptText,true);
                                    }
                                    else
                                    {
                                        session.setAttribute("name",abName);
                                        session.setAttribute("limit",String.valueOf(resultsResult.size()));
                                        session.setAttribute("start",String.valueOf(0));

                                        StringBuilder stringBuilder = new StringBuilder();

                                        for(int i=0; i<resultsResult.size(); i++)
                                        {
                                            if (i == resultsResult.size() - 1)
                                            {
                                                stringBuilder.append(String.valueOf(resultsResult.size())).append(". ");
                                            }
                                            else
                                            {
                                                stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                            }

                                            AbbreResultsResult result = resultsResult.get(i);

                                            if (result.getDefinition() != null)
                                            {
                                                stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                            }

                                            if (result.getCategoryname() != null)
                                            {
                                                stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                            }

                                            if (result.getParentcategoryname() != null)
                                            {
                                                stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                            }
                                        }

                                        String speechText = "Abbreviations of " + abName + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + abName ,speechText,rePromptText,true);
                                    }
                                }
                                else
                                {
                                    return getSimpleCardWithTextSpeechLetResponse("Abbreviation of " + abName ,fallBackSpeechText,fallBackRePromptText,true);
                                }
                            }
                            else
                            {
                                return getSimpleCardWithTextSpeechLetResponse("Abbreviation of " + abName ,fallBackSpeechText,fallBackRePromptText,true);
                            }
                        }
                        else
                        {
                            return getSimpleCardWithTextSpeechLetResponse("Abbreviation of " + abName ,fallBackSpeechText,fallBackRePromptText,true);
                        }
                    }
                    else
                    {
                        return getSimpleCardWithTextSpeechLetResponse("Abbreviation of " + abName ,fallBackSpeechText,fallBackRePromptText,true);
                    }
                }
                else
                {
                    return getFallbackResponse();
                }
            }
            else
            {
                return getFallbackResponse();
            }
        }
        else if (intentName != null && intentName.equals("MoreIntent"))
        {
            String name = getStoredSessionName(session);
            String limit = getStoredSessionLimit(session);

            if (!name.equals("") && !limit.equals(""))
            {
                int lim = Integer.parseInt(limit);

                if (lim != -1)
                {
                    if (!getResponseForName(name).equals(""))
                    {
                        String abResponse = getResponseForName(name);

                        AbbreResponse response = new Gson().fromJson(abResponse,AbbreResponse.class);

                        if (response != null)
                        {
                            AbbreResults abbreResults = response.getResults();

                            if (abbreResults != null)
                            {
                                List<AbbreResultsResult> resultsResult = abbreResults.getResult();

                                if (resultsResult != null && resultsResult.size() > 0)
                                {
                                    if (resultsResult.size() > 50)
                                    {
                                        if (lim == resultsResult.size())
                                        {
                                            String speechText = "I found only " + String.valueOf(lim) + " abbreviations using the name \"" + name + "\". I could not find more abbreviations. So, please say new name to get the abbreviation or acronym";

                                            String rePromptText = "please give new name to get the abbreviation or acronym";

                                            return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                                        }
                                        else if (lim > resultsResult.size())
                                        {
                                            String speechText = "I could not find more abbreviations using the name \"" + name + "\". So, please say new name to get the abbreviation or acronym";

                                            String rePromptText = "please give new name to get the abbreviation or acronym";

                                            return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                                        }
                                        else
                                        {
                                            int finalLimit = lim + 51;

                                            if (finalLimit >= resultsResult.size())
                                            {
                                                session.setAttribute("name",name);
                                                session.setAttribute("limit",String.valueOf(resultsResult.size()));
                                                session.setAttribute("start",String.valueOf(lim+1));

                                                List<AbbreResultsResult> newResultsResult = resultsResult.subList(lim+1,resultsResult.size());

                                                StringBuilder stringBuilder = new StringBuilder();

                                                for(int i=0; i<newResultsResult.size(); i++)
                                                {
                                                    if (i == newResultsResult.size() - 1)
                                                    {
                                                        stringBuilder.append(String.valueOf(newResultsResult.size())).append(". ");
                                                    }
                                                    else
                                                    {
                                                        stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                                    }

                                                    AbbreResultsResult result = newResultsResult.get(i);

                                                    if (result.getDefinition() != null)
                                                    {
                                                        stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                                    }

                                                    if (result.getCategoryname() != null)
                                                    {
                                                        stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                                    }

                                                    if (result.getParentcategoryname() != null)
                                                    {
                                                        stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                                    }
                                                }

                                                String speechText = "Abbreviations of " + name + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                                String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                                return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + name ,speechText,rePromptText,true);
                                            }
                                            else
                                            {
                                                session.setAttribute("name",name);
                                                session.setAttribute("limit",String.valueOf(finalLimit));
                                                session.setAttribute("start",String.valueOf(lim+1));

                                                List<AbbreResultsResult> newResultsResult = resultsResult.subList(lim+1,finalLimit);

                                                StringBuilder stringBuilder = new StringBuilder();

                                                for(int i=0; i<newResultsResult.size(); i++)
                                                {
                                                    if (i == newResultsResult.size() - 1)
                                                    {
                                                        stringBuilder.append(String.valueOf(newResultsResult.size())).append(". ");
                                                    }
                                                    else
                                                    {
                                                        stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                                    }

                                                    AbbreResultsResult result = newResultsResult.get(i);

                                                    if (result.getDefinition() != null)
                                                    {
                                                        stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                                    }

                                                    if (result.getCategoryname() != null)
                                                    {
                                                        stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                                    }

                                                    if (result.getParentcategoryname() != null)
                                                    {
                                                        stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                                    }
                                                }

                                                String speechText = "Abbreviations of " + name + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                                String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                                return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + name ,speechText,rePromptText,true);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        String speechText = "I found only " + String.valueOf(lim) + " abbreviations using the name \"" + name + "\". I could not find more abbreviations. So, please say new name to get the abbreviation or acronym";

                                        String rePromptText = "please give new name to get the abbreviation or acronym";

                                        return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                                    }
                                }
                                else
                                {
                                    String speechText = "I could not find more abbreviations using the name \"" + name + "\". So, please say new name to get the abbreviation or acronym";

                                    String rePromptText = "please give new name to get the abbreviation or acronym";

                                    return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                                }
                            }
                            else
                            {
                                String speechText = "I could not find more abbreviations using the name \"" + name + "\". So, please say new name to get the abbreviation or acronym";

                                String rePromptText = "please give new name to get the abbreviation or acronym";

                                return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                            }
                        }
                        else
                        {
                            String speechText = "I could not find more abbreviations using the name \"" + name + "\". So, please say new name to get the abbreviation or acronym";

                            String rePromptText = "please give new name to get the abbreviation or acronym";

                            return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                        }
                    }
                    else
                    {
                        String speechText = "I could not find more abbreviations using the name \"" + name + "\". So, please say new name to get the abbreviation or acronym";

                        String rePromptText = "please give new name to get the abbreviation or acronym";

                        return getSimpleCardWithTextSpeechLetResponse("More abbreviations of " + name,speechText,rePromptText,true);
                    }
                }
                else
                {
                    return getMoreFallbackResponse();
                }
            }
            else
            {
                return getMoreFallbackResponse();
            }
        }
        else if (intentName != null && intentName.equals("AMAZON.RepeatIntent"))
        {
            String name = getStoredSessionName(session);
            String limit = getStoredSessionLimit(session);
            String start = getStoredSessionStart(session);

            if (!name.equals("") && !limit.equals("") && !start.equals(""))
            {
                int lim = Integer.parseInt(limit);
                int sta = Integer.parseInt(start);

                if (lim != -1)
                {
                    if (!getResponseForName(name).equals(""))
                    {
                        String abResponse = getResponseForName(name);

                        AbbreResponse response = new Gson().fromJson(abResponse,AbbreResponse.class);

                        if (response != null)
                        {
                            AbbreResults abbreResults = response.getResults();

                            if (abbreResults != null)
                            {
                                List<AbbreResultsResult> resultsResult = abbreResults.getResult();

                                if (resultsResult != null && resultsResult.size() > 0)
                                {
                                    if (lim >= resultsResult.size())
                                    {
                                        session.setAttribute("name",name);
                                        session.setAttribute("limit",String.valueOf(resultsResult.size()));
                                        session.setAttribute("start",String.valueOf(sta));

                                        List<AbbreResultsResult> newResultsResult = resultsResult.subList(sta,resultsResult.size());

                                        StringBuilder stringBuilder = new StringBuilder();

                                        for(int i=0; i<newResultsResult.size(); i++)
                                        {
                                            if (i == newResultsResult.size() - 1)
                                            {
                                                stringBuilder.append(String.valueOf(newResultsResult.size())).append(". ");
                                            }
                                            else
                                            {
                                                stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                            }

                                            AbbreResultsResult result = newResultsResult.get(i);

                                            if (result.getDefinition() != null)
                                            {
                                                stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                            }

                                            if (result.getCategoryname() != null)
                                            {
                                                stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                            }

                                            if (result.getParentcategoryname() != null)
                                            {
                                                stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                            }
                                        }

                                        String speechText = "Abbreviations of " + name + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + name ,speechText,rePromptText,true);
                                    }
                                    else
                                    {
                                        session.setAttribute("name",name);
                                        session.setAttribute("limit",String.valueOf(lim));
                                        session.setAttribute("start",String.valueOf(sta));

                                        List<AbbreResultsResult> newResultsResult = resultsResult.subList(sta,lim);

                                        StringBuilder stringBuilder = new StringBuilder();

                                        for(int i=0; i<newResultsResult.size(); i++)
                                        {
                                            if (i == newResultsResult.size() - 1)
                                            {
                                                stringBuilder.append(String.valueOf(newResultsResult.size())).append(". ");
                                            }
                                            else
                                            {
                                                stringBuilder.append(String.valueOf(i + 1)).append(". ");
                                            }

                                            AbbreResultsResult result = newResultsResult.get(i);

                                            if (result.getDefinition() != null)
                                            {
                                                stringBuilder.append("Definition : ").append(result.getDefinition()).append(",\n");
                                            }

                                            if (result.getCategoryname() != null)
                                            {
                                                stringBuilder.append("Category : ").append(result.getCategoryname()).append(",\n");
                                            }

                                            if (result.getParentcategoryname() != null)
                                            {
                                                stringBuilder.append("Parent Category : ").append(result.getParentcategoryname()).append(".\n");
                                            }
                                        }

                                        String speechText = "Abbreviations of " + name + " are listed below : " + stringBuilder.toString() + ". If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        String rePromptText = "If you want to more say \"more\" or if you could not understand the above abbreviation, if you want to ask that again, say \"repeat\" or if you want to get abbreviation or acronym of new name, simply say \" the new abbreviation or acronym name \"";

                                        return getSimpleCardWithTextSpeechLetResponse("Abbreviations of " + name ,speechText,rePromptText,true);
                                    }
                                }
                                else
                                {
                                    return getRepeatFallbackResponse();
                                }
                            }
                            else
                            {
                                return getRepeatFallbackResponse();
                            }
                        }
                        else
                        {
                            return getRepeatFallbackResponse();
                        }
                    }
                    else
                    {
                        return getRepeatFallbackResponse();
                    }
                }
                else
                {
                    return getRepeatFallbackResponse();
                }
            }
            else
            {
                return getRepeatFallbackResponse();
            }
        }
        else if (intentName != null && intentName.equals("AMAZON.FallbackIntent"))
        {
            return getFallbackResponse();
        }
        else if (intentName != null && intentName.equals("AMAZON.HelpIntent"))
        {
            return getHelpResponse();
        }
        else if (intentName != null && intentName.equals("AMAZON.StopIntent"))
        {
            return getStopOrCancelResponse();
        }
        else if (intentName != null && intentName.equals("AMAZON.CancelIntent"))
        {
            return getStopOrCancelResponse();
        }
        else if (intentName != null && intentName.equals("AMAZON.YesIntent"))
        {
            return getYesResponse();
        }
        else if (intentName != null && intentName.equals("AMAZON.NoIntent"))
        {
            return getNoResponse();
        }
        else
        {
            return getFallbackResponse();
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> speechletRequestEnvelope)
    {

    }

    private String getStoredSessionName(Session session)
    {
        String name = (String) session.getAttribute("name");

        if (name != null)
        {
            return name;
        }
        else
        {
            return "";
        }
    }

    private String getStoredSessionLimit(Session session)
    {
        String limit = (String) session.getAttribute("limit");

        if (limit != null)
        {
            return limit;
        }
        else
        {
            return "";
        }
    }

    private String getStoredSessionStart(Session session)
    {
        String start = (String) session.getAttribute("start");

        if (start != null)
        {
            return start;
        }
        else
        {
            return "";
        }
    }

    private String getResponseForName(String name)
    {
        try
        {
            URL urlDetail = new URL("https://www.abbreviations.com/services/v2/abbr.php?uid=6512&tokenid=VZxTuZEHrTX11uV5&term=" + name);

            HttpsURLConnection connection = (HttpsURLConnection) urlDetail.openConnection();

            connection.setDoOutput(true);

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/xml");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            StringBuilder resultBuilder = new StringBuilder();

            String jsonOutput;

            while ((jsonOutput = bufferedReader.readLine()) != null)
            {
                resultBuilder.append(jsonOutput);
            }

            try
            {
                org.json.JSONObject jsonObject = XML.toJSONObject(resultBuilder.toString());

                return jsonObject.toString();
            }
            catch (JSONException e)
            {
                return "";
            }
        }
        catch (IOException e)
        {
            return "";
        }
    }

    private SpeechletResponse getWelcomeResponse()
    {
        String speechText = "Hi, welcome to Alexa Abbreviation Desk. It's a pleasure to talk to you. " +
                "If you want abbreviation or acronym of any name, i can give abbreviation or acronym of that name. " +
                "If you want more instructions or help simply say 'help'. " +
                "Ok, now you can start to say any abbreviation or acronym name";

        String cardTitle = "Welcome";

        String rePromptText = "Ok, now you can start to say any abbreviation or acronym name";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePromptText,true);
    }

    private SpeechletResponse getMoreFallbackResponse()
    {
        String speechText = "I could not find any name to give more abbreviations or acronyms. Please, say a new name to get the abbreviations or acronyms.";

        String rePrompt = "Please, say a new name to get the abbreviations or acronyms.";

        String cardTitle = "Repeat";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePrompt,true);
    }

    private SpeechletResponse getRepeatFallbackResponse()
    {
        String speechText = "I could not find any name to repeat the abbreviations or acronyms. Please, say a new name to get the abbreviations or acronyms.";

        String rePrompt = "Please, say a new name to get the abbreviations or acronyms.";

        String cardTitle = "Repeat";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePrompt,true);
    }

    private SpeechletResponse getFallbackResponse()
    {
        String speechText = "Oops..There was some internal or server problem, don't worry. \nPlease say that the name again.";

        String cardTitle = "Problem";

        String rePromptText = "please say that the name again.";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePromptText,true);
    }

    private SpeechletResponse getHelpResponse()
    {
        String speechText = "It pleasure to help you. \n" +
                "If you have any doubts or you don't know how to ask to 'Abbreviation Desk', don't worry. \n" +
                "I clarify your doubts. If you tell any abbreviation or acronym name, i can give abbreviation or acronym of that name. " +
                "I can give some examples to how to ask to Abbreviation Desk, ok now let's start, you say \"ASAP\" and category medical, i can give the definition,category name and parent category name. " +
                "Ok, now you can start to say any abbreviation or acronym name ";

        String cardTitle = "Help";

        String rePromptText = "Ok, now you can start to say any abbreviation or acronym name ";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePromptText,true);
    }

    private SpeechletResponse getStopOrCancelResponse()
    {
        String speechText = "Would you like to cancel or stop all the conversations?. If you want to cancel or stop say 'yes' or if you want to continue the conversation say 'no'";

        String cardTitle = "Stop or Cancel";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,speechText,true);
    }

    private SpeechletResponse getYesResponse()
    {
        String speechText = "Ok, i stopped the all conversations. If you like to speak to me again. Simply you can ask or say \"Alexa, open abbreviation desk and abbreviation of \" your abbreviation name \".";

        return getSimpleCardWithTextSpeechLetResponse("Bye!",speechText,speechText,false);
    }

    private SpeechletResponse getNoResponse()
    {
        String speechText = "Ok, don't worry. We can continue the conversation. \nPlease, say any abbreviation or acronym name";

        String cardTitle = "Continue Conversation";

        String rePrompt = "Please, say any abbreviation or acronym name";

        return getSimpleCardWithTextSpeechLetResponse(cardTitle,speechText,rePrompt,true);
    }

    private SpeechletResponse getSimpleCardWithTextSpeechLetResponse(String cardTitle,String speechText, String repromptText, boolean isAskResponse)
    {
        SimpleCard card = new SimpleCard();
        card.setTitle(cardTitle);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse)
        {
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        }
        else
        {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}
