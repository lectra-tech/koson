# Contributing

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change. 

## Reporting Bugs

This section guides you through submitting a bug report for the project. Following these guidelines helps maintainers and the community understand your report, reproduce the behavior, and find related reports.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

### Before Submitting A Bug Report

* Check the open issues to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

### How Do I Submit A (Good) Bug Report?

Explain the problem and include additional details to help maintainers reproduce the problem:

* **Use a clear and descriptive title** for the issue to identify the problem.
* **Describe the exact steps which reproduce the problem** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include links to files or GitHub projects, or copy/pasteable snippets, which you use in those examples. If you're providing snippets in the issue, use [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
* **Explain which behavior you expected to see instead and why.**

## Pull Request Process

The core team will be monitoring for pull requests. When we get one, we'll need to get another person to sign off on the changes and then merge the pull request. We'll do our best to provide updates and feedback throughout the process.

_Before_ submitting a pull request, please make sure the following is done…

1.  Fork the repo and create your branch from `master`. A guide on how to fork a repository: https://help.github.com/articles/fork-a-repo/

    Open terminal (e.g. Terminal, iTerm, Git Bash or Git Shell) and type:

    ```sh
    git clone https://github.com/<your_username>/ld-react-feature-flags
    cd ld-react-feature-flagsst
    git checkout -b my_branch
    ```

    Note: Replace `<your_username>` with your GitHub username

2.  Check you can run tests by typing `mvn package`

3.  If you've added code it must be tested. Check the tests you've added work by doing

    ```sh
    # in the background
    mvn clean package
    ```

4.  If you've changed APIs, update the documentation.

5.  Open a pull request for review
