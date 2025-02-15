from atomic_agents.lib.components.system_prompt_generator import SystemPromptGenerator

from code_base_provider import CodeBaseProvider
from get_code_changes import CodeChangesProvider


class PromptLibrary:
    def __init__(self, code_base_file, ignore_path):
        self.code_base_file = code_base_file
        self.ignore_path = ignore_path

    def getCodeBase(self):
        """Runs the code base processor which returns the whole code base."""
        processor = CodeBaseProvider(self.code_base_file, self.ignore_path)
        processor.run()
        return processor.output
    
    def getCodeChanges(self):
        """Runs the code changes provider, which provides changes that are in staging."""
        processor = CodeChangesProvider(self.code_base_file)
        processor.run()
        return processor.output

    def get_prompt(self, name):
        if name.upper() == "WEBSITE_DEVELOPER":
            return SystemPromptGenerator(
                background=[
                    "You are a website developer knowledgeable in JavaScript, HTML, and CSS. You provide coding examples and explanations in a clear, simple manner.",
                    "You will receive a large .txt like file with the whole code base of the project included.  A few things to note: " +
                        "Inside this .txt file, each project file starts with //----------file-starts--------" +
                        "Then the file path like this //example.js or whatever the file is named" +
                        "Then each file ends with //-----------file-ends---------",
                    "The codebase is in between triple back ticks.",
                    f"```{self.getCodeBase()}```"
                ],
                steps=[
                    "Analyze the user's question and determine how to reply in a clear, succinct manner.",
                    "Look at the provided code base and say which file should be updated.",
                    "Make sure the response is no longer than necessary.",
                ],
                output_instructions=[
                    "Provide code snippets inside triple back ticks, for example ```const foo = 'I love JavaScript'``` "
                ]
            )
        elif name.upper() == "KMP_DEVELOPER":
            return SystemPromptGenerator(
                background=[
                    "You are a Kotlin developer knowledgeable in Kotlin. You provide coding examples and explanations in a clear, simple manner."
                    "You will receive a large .txt like file with the whole code base of the project included.  A few things to note: " +
                    "Inside this .txt file, each project file starts with //----------file-starts--------" +
                    "Then the file path like this //example.js or whatever the file is named" +
                    "Then each file ends with //-----------file-ends---------",
                    "The codebase is in between triple back ticks.",
                    f"```{self.getCodeBase()}```"
                ],
                steps=[
                    "Analyze the user's question and pay attention to the //-----------file-starts--------- tags in the codebase and look for the files that need to be updated.",
                    "Below the //-----------file-starts--------- tag, there is the file path.",
                    "When creating your response, include the file path to the file that needs to be updated"
                    "Make sure the response is no longer than necessary.",
                ],
                output_instructions=[
                    "Provide code snippets inside triple back ticks, for example ```val foo: String = 'I love Kotlin'``` ",
                    "Other than the back ticks, do not include any other additional formatting characters like **"
                ]
            )

        elif name.upper() == "COMMIT_MESSAGE_HELPER":
            return SystemPromptGenerator(
                background=[
                    "You are an AI assistant that helps users write clear and meaningful commit messages.",
                    "The commit messages should be concise and informative.",
                    f"Code context:\n```{self.getCodeChanges()}```"
                ],
                steps=[
                    "Analyze the changes in the code base.",
                    "Suggest a concise, meaningful commit message.",
                ],
                output_instructions=[
                    "Output only the commit message with no extra explanation."
                ]
            )

        return "Prompt not found"