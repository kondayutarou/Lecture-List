FROM openjdk:11

ENV ANDROID_COMPILE_SDK "32"
ENV ANDROID_BUILD_TOOLS "30.0.3"
ENV ANDROID_SDK_TOOLS "8092744"
ENV RUBY_VERSION "3.1.0"

# install OS packages
RUN apt --quiet update --yes
RUN apt --quiet install --yes wget tar unzip lib32stdc++6 lib32z1

# install Ruby using ruby-build
RUN apt --quiet install --yes autoconf bison build-essential libssl-dev libyaml-dev libreadline6-dev zlib1g-dev libncurses5-dev libffi-dev libgmp-dev
RUN git clone https://github.com/rbenv/ruby-build.git
RUN PREFIX=/usr/local $PWD/ruby-build/install.sh
RUN ruby-build $RUBY_VERSION $PWD/local/ruby-$RUBY_VERSION
ENV PATH $PATH:$PWD/local/ruby-$RUBY_VERSION/bin/

# We use this for xxd hex->binary
RUN apt --quiet install --yes vim-common

# install Android SDK
RUN wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
RUN unzip -q -d android-sdk-linux android-sdk.zip
ENV ANDROID_HOME $PWD/android-sdk-linux
ENV PATH $ANDROID_HOME/cmdline-tools/bin:$PATH
RUN echo y | sdkmanager --sdk_root=${ANDROID_HOME} "platforms;android-${ANDROID_COMPILE_SDK}" >/dev/null
RUN echo y | sdkmanager --sdk_root=${ANDROID_HOME} "platform-tools" >/dev/null
RUN echo y | sdkmanager --sdk_root=${ANDROID_HOME} "build-tools;${ANDROID_BUILD_TOOLS}" >/dev/null
RUN yes | sdkmanager --sdk_root=${ANDROID_HOME} --licenses

# install FastLane
RUN gem install bundler --no-document
